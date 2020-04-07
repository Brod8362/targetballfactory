package pw.byakuren.tbf.command
import net.dv8tion.jda.api.entities.Message

class HelpCommand(commandRegistry: CommandRegistry) extends Command {
  override val name: String = "help"
  override val desc: String = "See command help"

  override def run(m: Message, args: Array[String]): Unit = {
    m.getChannel.sendMessage(commandRegistry.all.map(c => f"[${c.name}: ${c.desc}").mkString("```","\n","```")).queue()
  }
}
