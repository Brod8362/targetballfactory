package pw.byakuren.tbf.command
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.actions.XPTestAction
import pw.byakuren.tbf.util.Utility._

class XPCommand extends Command {
  override val name: String = "xp"
  override val desc: String = "give xp"

  override def run(m: Message, args: Array[String]): Unit = {
    val eu = m.getAuthor.economyUser()
    val amt = args(0).toInt
    eu match {
      case Some(x) => x.perform(new XPTestAction(m, amt))
      case None => m.getChannel.sendMessage("you don't exist")
    }
  }
}
