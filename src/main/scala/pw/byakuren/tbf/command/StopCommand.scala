package pw.byakuren.tbf.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.sql.SQLConnection
import pw.byakuren.tbf.tracking.EconomyUserTracker

class StopCommand(implicit sql: SQLConnection) extends Command {
  override val name: String = "stop"
  override val desc: String = "stop the bot"

  override def run(m: Message, args: Array[String]): Unit = {
    if (m.getMember.hasPermission(Permission.ADMINISTRATOR)) {
      m.getChannel.sendMessage("bye").complete()
      EconomyUserTracker.write()
      System.exit(0)
    }
  }
}
