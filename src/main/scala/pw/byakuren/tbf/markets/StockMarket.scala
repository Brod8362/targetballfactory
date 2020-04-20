package pw.byakuren.tbf.markets

import java.awt.Color

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import pw.byakuren.tbf.SQLConnection
import pw.byakuren.tbf.chart.ChartCreator
import pw.byakuren.tbf.inventory.StockItem
import pw.byakuren.tbf.util.SQLWritable

class StockMarket(val name: String, val id: Int, maxGrowth: Float, maxDecay: Float, iterMean: Int, iterOffset: Int,
                  baseValue: Double, lean: Float, fun: Float, crash: Float, jump: Float)
                 (implicit sql: SQLConnection) extends SQLWritable {

  var value: Double = baseValue
  var previousValues: Seq[Double] = Seq(value)
  private var stockChannel:Option[TextChannel] = None
  var stockItem: Option[StockItem] = None

  /*
  name : string : name of market
  max-growth : float (0.0-1.0) : max increase in stock per iter, as a %
  max-decay  : float (0.0-1.0) : max decrease in stock per iter, as a %
  iter-mean : int : average time between iterations (seconds)
  iter-offset : int : maximum time difference from iter-mean (seconds)
  base-value : double : starting value of stock
  lean : float (0.0-1.0) : which direction the market tends to lean in terms of growth/decay. 0.5 is neutral
  fun : float (0.0-1.0) : chance to either crash the stock or skyrocket, using inverse of the lean value
  crash : float (0.0-1.0) : up to how much (%) the stock may lose when it crashes
  jump : float (0.0-1.0) up to how much (%) the stock may gain when it skyrockets
   */

  def setCallbackChannel(channel: TextChannel): Unit = stockChannel = Some(channel)

  def restoreValues(previous: Seq[Double]): Unit = {
    this.value =previous.last
    this.previousValues = previous
  }

  def grow(): Unit = updateValue(value*(1+(maxGrowth*Math.random())))

  def decay(): Unit = updateValue(value*(1-(Math.random()*maxDecay)))

  def skyrocket(): Unit = updateValue(value*(1+(jump*Math.random())))

  def crash(): Unit = updateValue(value*(1-(Math.random()*crash)))

  private def updateValue(newValue: Double): Unit = {
    val prev = value
    value = newValue max 0
    writeIncrementalValue
    previousValues=previousValues++Seq(value)
    val eb = new EmbedBuilder().setTitle(s"$name prices ${if (prev < value) "increase" else "fall"}")
      .setColor(if (prev < value) Color.GREEN else Color.RED)
      .addField("$ Change", f"$$$prev%.2f => $$$value%.2f", true)
      .addField("% Change", f"${((value/prev)-1)*100}%.2f%%", true)
    stockChannel match {
      case Some(x) => x.sendMessage(eb.build).addFile(ChartCreator(this), "chart.png").queue()
      case _ =>
    }
  }

  private def trigger(f:Float): Boolean = Math.random() < f

  private def shouldTriggerFun: Boolean = trigger(fun)

  private def fun(): Unit = {
    if (!shouldTriggerFun) return
    if (trigger(1-lean)) {
      skyrocket()
    } else {
      crash()
    }
  }

  def iterate(): Long = {
    if (trigger(lean)) {
      grow()
    } else {
      decay()
    }
    ((iterMean+((Math.random()*iterOffset*2)-iterOffset))*1000).toLong
  }

  def writeIncrementalValue(implicit sql: SQLConnection): Unit = {
    sql.writeMarketValue(this)
  }

  override def write(): Unit = {

  }
}
