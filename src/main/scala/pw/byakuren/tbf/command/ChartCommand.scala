package pw.byakuren.tbf.command
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.utils.AttachmentOption
import pw.byakuren.tbf.chart.ChartCreator
import pw.byakuren.tbf.markets.StockMarket

class ChartCommand(markets: Seq[StockMarket]) extends Command {
  override val name: String = "chart"
  override val desc: String = "view the chart for a market"

  override def run(m: Message, args: Array[String]): Unit = {
    args(0).toIntOption match {
      case Some(ind) =>
        Option(markets(ind-1)) match {
          case Some(market) =>
            m.getChannel.sendFile(ChartCreator(market), "chart.png").queue()
          case None =>
            m.getChannel.sendMessage(s"market not found withid $ind").queue()
        }
      case None =>
    }
  }
}
