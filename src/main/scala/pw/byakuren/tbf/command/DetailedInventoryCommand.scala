package pw.byakuren.tbf.command

import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.sql.SQLConnection
import pw.byakuren.tbf.tracking.EconomyUserTracker

class DetailedInventoryCommand(implicit val sql: SQLConnection) extends Command {
  override val name: String = "dinv"
  override val desc: String = "view your inventory in detail (with indices)"

  override def run(m: Message, args: Array[String]): Unit = {
    EconomyUserTracker.apply(m.getAuthor) match {
      case Some(eu) =>
        var fs = "__Inventory__\n"
        for (i <- eu.inventory.a.indices) {
          val x = eu.inventory.a(i)
          if (x != null) {
            fs+=f"$i:${x.name} ($$${x.value}%.2f)\n"
          }
        }
        m.getAuthor.openPrivateChannel().complete().sendMessage(fs).queue()
      case None =>
        m.getChannel.sendMessage("you don't exist")
    }
  }
}
