package pw.byakuren.tbf.command
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.tracking.EconomyUserTracker

class InventoryCommand extends Command {
  override val name: String = "inv"
  override val desc: String = "view your inventory"

  override def run(m: Message, args: Array[String]): Unit = {
    EconomyUserTracker.apply(m.getAuthor) match {
      case Some(eu) =>
        m.getChannel.sendMessage(eu.inventory.toString).queue()
      case None =>
        m.getChannel.sendMessage("you don't exist")
    }
  }
}
