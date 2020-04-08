package pw.byakuren.tbf.command

import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.actions.{BuyStockAction, GiveMoneyAction}
import pw.byakuren.tbf.util.Emoji
import pw.byakuren.tbf.util.Utility._

class BuyStockCommand extends Command {
  override val name: String = "buystock"
  override val desc: String = "buy stock"

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
      case Some(x) => x.perform(new BuyStockAction(m, amt))
      case None => m.getChannel.sendMessage("you don't exist")
    }
  }
}
