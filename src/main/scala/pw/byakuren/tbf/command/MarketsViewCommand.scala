package pw.byakuren.tbf.command
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.markets.StockMarket

class MarketsViewCommand(a: Array[StockMarket]) extends Command {
  override val name: String = "markets"
  override val desc: String = "View stock markets"

  override def run(m: Message): Unit = {
    var i = 0
    val str = for (s <- a) yield {
      i+=1 // this is necessary because scala is STUPID
      f"[${i}]${s.name} - ${sign(s.value)}$$${s.value}%.2f"
    }
    m.getChannel.sendMessage(s"```diff\n${str.mkString("\n")}```").queue()

  }

  private def sign(v: Double): Char = {
    if (v < 0) '-' else '+'
  }
}
