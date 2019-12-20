package pw.byakuren.tbf

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.{JDA, JDABuilder}
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EconomyBot(token: String) extends ListenerAdapter {

  val jda:JDA = new JDABuilder(token).addEventListeners(this).build()

  override def onReady(event: ReadyEvent): Unit = println("Logged in")

}
