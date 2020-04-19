package pw.byakuren.tbf

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.{JDA, JDABuilder}
import net.dv8tion.jda.api.hooks.ListenerAdapter
import pw.byakuren.tbf.actions.GiveMoneyAction
import pw.byakuren.tbf.command.{BuyStockCommand, ChartCommand, CommandRegistry, DetailedInventoryCommand, HelpCommand, InventoryCommand, MarketsViewCommand, MeCommand, MoneyCommand, SellCommand, XPCommand}
import pw.byakuren.tbf.inventory.ItemRegistry
import pw.byakuren.tbf.markets.{StockMarket, StockMarketThreadManager}
import pw.byakuren.tbf.targetball.TargetBallThread
import pw.byakuren.tbf.util.{Channels, Emoji}

class EconomyBot(token: String, prefix:String, markets: Seq[StockMarket], stockChannelId: Long, ballChannelId: Long) extends ListenerAdapter {

  val jda:JDA = new JDABuilder(token).addEventListeners(this).build()
  val registry:CommandRegistry = new CommandRegistry()
  val marketManager = new StockMarketThreadManager

  var channels: Option[Channels] = None

  override def onReady(event: ReadyEvent): Unit = {
    channels = Some(new Channels(jda, stockChannelId, ballChannelId))
    println(s"Logged in\nMarkets: ${markets.length}")
    ItemRegistry.generateStockItems(markets)

    registry.register(new MarketsViewCommand(markets))
    registry.register(new MeCommand())
    registry.register(new XPCommand())
    registry.register(new MoneyCommand)
    registry.register(new HelpCommand(registry))
    registry.register(new BuyStockCommand)
    registry.register(new InventoryCommand)
    registry.register(new DetailedInventoryCommand)
    registry.register(new SellCommand)
    registry.register(new ChartCommand(markets))

    channels.foreach(c => markets.foreach(_.setCallbackChannel(c.stockChannel)))

    TargetBallThread.channels = channels
    TargetBallThread.start()
    marketManager.start(markets)


//    while (true) {
//      for (market <- markets) {
//        market.iterate()
//        Thread.sleep(5000L)
//      }
//    }
  }

  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    val m = event.getMessage
    val content = m.getContentRaw
    if (m.getAuthor.isBot || content.substring(0, prefix.length)!=prefix) return
    val split = content.substring(prefix.length).split(" ")
    registry.find(split(0)) match {
      case Some(x) =>
        new Thread(() => x.run(m, split.slice(1, split.length))).start()
      case _ =>
        m.addReaction(Emoji.checkmark).queue()
    }
  }
}
