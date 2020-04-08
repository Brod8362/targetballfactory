package pw.byakuren.tbf.command

import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.actions.{GiveMoneyAction, XPTestAction}
import pw.byakuren.tbf.util.Emoji
import pw.byakuren.tbf.util.Utility._

class MoneyCommand extends Command {
  override val name: String = "money"
  override val desc: String = "give money"

  override def run(m: Message, args: Array[String]): Unit = {
    val eu = m.getAuthor.economyUser()
    val amt = try {
      args(0).toInt
    } catch {
      case x: NumberFormatException =>
        m.addReaction(Emoji.x).queue()
        0
      case _ => 0
    }
    eu match {
      case Some(x) => x.perform(new GiveMoneyAction(m, amt))
      case None => m.getChannel.sendMessage("you don't exist")
    }
  }
}
