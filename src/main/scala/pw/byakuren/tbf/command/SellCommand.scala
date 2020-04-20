package pw.byakuren.tbf.command

import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.actions.GiveMoneyAction
import pw.byakuren.tbf.inventory.Inventory
import pw.byakuren.tbf.sql.SQLConnection
import pw.byakuren.tbf.util.Emoji
import pw.byakuren.tbf.util.Utility._

class SellCommand(implicit sql: SQLConnection) extends Command {
  override val name: String = "sell"
  override val desc: String = "sell an item"

  override def run(m: Message, args: Array[String]): Unit = {
    val eu = m.getAuthor.economyUser()
    val index = try {
      args(0).toInt
    } catch {
      case x: NumberFormatException =>
        m.addReaction(Emoji.x).queue()
        -1
      case _ => -1
    }
    if (index < 0 || index >= Inventory.InventorySize) {
      m.getChannel.sendMessage("Invalid index").queue()
    }
    eu match {
      case Some(user) =>
        user.inventory(index) match {
          case Some(item) =>
            user.inventory.remove(index)
            user.balance.deposit(item.value)
            m.getChannel.sendMessage(s"Sold ${item.name} for ${item.value}").queue()
          case None =>
            m.getChannel.sendMessage(s"No item at $index").queue()
        }
      case None => m.getChannel.sendMessage("you don't exist")
    }
  }
}
