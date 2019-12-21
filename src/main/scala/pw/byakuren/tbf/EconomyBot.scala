package pw.byakuren.tbf

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.{JDA, JDABuilder}
import net.dv8tion.jda.api.hooks.ListenerAdapter
import pw.byakuren.tbf.markets.StockMarket

class EconomyBot(token: String, markets: Array[StockMarket]) extends ListenerAdapter {

  val jda:JDA = new JDABuilder(token).addEventListeners(this).build()

  override def onReady(event: ReadyEvent): Unit = println(s"Logged in\nMarkets: ${markets.size}")

}
