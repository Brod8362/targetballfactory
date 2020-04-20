package pw.byakuren.tbf

import com.sedmelluq.discord.lavaplayer.player.{AudioLoadResultHandler, DefaultAudioPlayerManager}
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.{AudioPlaylist, AudioTrack}
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.{JDA, JDABuilder}
import net.dv8tion.jda.api.hooks.ListenerAdapter
import pw.byakuren.tbf.actions.GiveMoneyAction
import pw.byakuren.tbf.audio.{AudioPlayerSendHandler, LoopScheduler}
import pw.byakuren.tbf.command.{BuyStockCommand, ChartCommand, CommandRegistry, DetailedInventoryCommand, HelpCommand, InventoryCommand, MarketsViewCommand, MeCommand, MoneyCommand, SellCommand, StopCommand, XPCommand}
import pw.byakuren.tbf.inventory.ItemRegistry
import pw.byakuren.tbf.markets.{StockMarket, StockMarketThreadManager}
import pw.byakuren.tbf.sql.SQLConnection
import pw.byakuren.tbf.targetball.TargetBallThread
import pw.byakuren.tbf.tracking.EconomyUserTracker
import pw.byakuren.tbf.util.{Channels, Emoji}

class EconomyBot(token: String, prefix: String, markets: Seq[StockMarket], stockChannelId: Long, ballChannelId: Long,
                 loopChannelId: Long, loopSong: String)(implicit sql: SQLConnection) extends ListenerAdapter {

  val jda: JDA = new JDABuilder(token).addEventListeners(this).build()
  val registry: CommandRegistry = new CommandRegistry()
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
    registry.register(new StopCommand)

    channels.foreach(c => markets.foreach(_.setCallbackChannel(c.stockChannel)))

    TargetBallThread.create()
    TargetBallThread.channels = channels
    TargetBallThread.start()
    marketManager.start(markets)

    Option(jda.getVoiceChannelById(loopChannelId)) match {
      case Some(loopChannel) =>
        initAudioLoop(jda, loopChannel, loopSong)
      case None =>
        println("can't find loop voice channel")
    }

    new Thread(() => {
      Thread.sleep(60000*5) //sleep 5 min
      EconomyUserTracker.write()
    }).start()
  }

  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    val m = event.getMessage
    val content = m.getContentRaw
    if (m.getAuthor.isBot || content.substring(0, prefix.length) != prefix) return
    val split = content.substring(prefix.length).split(" ")
    registry.find(split(0)) match {
      case Some(x) =>
        new Thread(() => x.run(m, split.slice(1, split.length))).start()
      case _ =>
        m.addReaction(Emoji.checkmark).queue()
    }
  }

  def initAudioLoop(jda: JDA, channel: VoiceChannel, loopURL: String): Unit = {
    val apm = new DefaultAudioPlayerManager
    AudioSourceManagers.registerRemoteSources(apm)
    val player = apm.createPlayer()
    player.addListener(new LoopScheduler)
    val am = channel.getGuild.getAudioManager
    am.openAudioConnection(channel)
    am.setSendingHandler(new AudioPlayerSendHandler(player))
    apm.loadItem(loopURL, new AudioLoadResultHandler {
      override def trackLoaded(track: AudioTrack): Unit = {
        println("loaded track")
        player.startTrack(track, false)
      }
      override def playlistLoaded(playlist: AudioPlaylist): Unit = ???
      override def noMatches(): Unit = {
        println(s"failed to load $loopURL: no matches")
      }
      override def loadFailed(exception: FriendlyException): Unit = {
        println(s"failed to load $loopURL: $exception")
      }
    })
  }
}
