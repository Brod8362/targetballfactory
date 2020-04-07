package pw.byakuren.tbf.targetball

import net.dv8tion.jda.api.entities.{Member, TextChannel}
import pw.byakuren.tbf.tracking.EconomyUserTracker
import pw.byakuren.tbf.util.Channels

import scala.jdk.CollectionConverters._

sealed class TargetBallThread extends Runnable {

  final val MinimumEnergy = 0
  var MaximumEnergy = 100

  var energy: Int = 100

  val minimumWait: Long = 300000 //5 minutes
  val maximumWait: Long = 6000000 //100 minutes

  override def run(): Unit = {
    while (true) {
      Thread.sleep(minimumWait+((maximumWait-minimumWait)*Math.random()).toLong) //wait random time between min and max
      for (channels <- TargetBallThread.channels) {
        val eu = EconomyUserTracker(randomUser(channels.targetBallChannel).getUser).get
        val maxBalls = EconomyUserTracker.cumulativeLevel/2
        val generated = (maxBalls * Math.random()).toInt
        val bonus = eu.level*Math.random*(1-(eu.xp/eu.totalXpRequired(eu.level+1)))


      }
    }
  }

  def charge(amount: Int): Unit = {
    energy+=amount
    if (energy < MinimumEnergy) energy = MinimumEnergy
    if (energy > MaximumEnergy) energy = MaximumEnergy
  }

  def randomUser(channel: TextChannel): Member = {
    val eligible = channel.getMembers.asScala.filter(!_.getUser.isBot)
    eligible((eligible.size*Math.random).toInt)
  }
}

object TargetBallThread {

  val runnable = new TargetBallThread
  var channels: Option[Channels] = None //this must be set before the thread starts for it to work properly
  var thread: Option[Thread] = None

  def start(): Unit = {
    thread = Some(new Thread(runnable))
    thread.foreach(_.start())
  }

  def stop(): Unit = {
    thread.foreach(_.stop())
  }

}
