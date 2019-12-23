package pw.byakuren.tbf.command

import net.dv8tion.jda.api.entities.Message

trait Command {

  val name:String

  val desc:String

  def run(m: Message, args: Array[String]):Unit

}
