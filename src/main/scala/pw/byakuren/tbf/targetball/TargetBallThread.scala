package pw.byakuren.tbf.targetball

import net.dv8tion.jda.api.entities.{Member, TextChannel}
import pw.byakuren.tbf.actions.BallsMadeAction
import pw.byakuren.tbf.sql.SQLConnection
import pw.byakuren.tbf.tracking.EconomyUserTracker
import pw.byakuren.tbf.util.{Channels, Embeds}

import scala.jdk.CollectionConverters._

sealed class TargetBallThread(implicit SQLConnection: SQLConnection) extends Runnable {

  final val MinimumEnergy = 0
  var MaximumEnergy = 100

  var energy: Int = 100
  def energyCost = 10

//  val minimumWait: Long = 300000 //5 minutes
//  val maximumWait: Long = 6000000 //100 minutes

  val minimumWait: Long = 15000 //15sec
  val maximumWait: Long = 60000 //1 minute

  var quotes: Seq[String] = Seq("witty thing")

  final val basicIcon = "\uD83E\uDD52"
  final val extraIcon = "<:spicy_pickle:697476294857981962>"

  override def run(): Unit = {
    while (true) {
      Thread.sleep(minimumWait+((maximumWait-minimumWait)*Math.random()).toLong) //wait random time between min and max
      for (channels <- TargetBallThread.channels) {
        while (energy<energyCost) {
          Thread.sleep(5000)
        }
        charge(-1*energyCost)
        val eu = EconomyUserTracker(randomUser(channels.targetBallChannel).getUser).get
        val maxBalls = EconomyUserTracker.cumulativeLevel/2
        val generated = (maxBalls * Math.random()).toInt
        val bonus = (eu.level/2)*Math.random*(1-(eu.xp/eu.totalXpRequired(eu.level+1)))
        val s = basicIcon.repeat(generated)+extraIcon.repeat(bonus.toInt)
        val quote = quotes((quotes.size*Math.random()).toInt)
        val context = channels.targetBallChannel.sendMessage(Embeds.ballsMade(eu.user, s, quote)).complete()
        eu.perform(new BallsMadeAction(context, bonus.toInt))
      }
    }
  }

  def charge(amount: Int): Unit = {
    energy+=amount
    energy = energy max MinimumEnergy
    energy = energy min MaximumEnergy
  }

  def randomUser(channel: TextChannel): Member = {
    val eligible = channel.getMembers.asScala.filter(!_.getUser.isBot)
    eligible((eligible.size*Math.random).toInt)
  }
}

object TargetBallThread {

  var runnable: TargetBallThread = null
  var channels: Option[Channels] = None //this must be set before the thread starts for it to work properly
  var thread: Option[Thread] = None

  def start(): Unit = {
    thread = Some(new Thread(runnable))
    thread.foreach(_.start())
  }

  def stop(): Unit = {
    thread.foreach(_.stop())
  }

  def create()(implicit SQLConnection: SQLConnection): Unit = {
    runnable = new TargetBallThread
  }

}
