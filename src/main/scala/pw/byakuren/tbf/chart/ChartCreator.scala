package pw.byakuren.tbf.chart

import java.awt.{BasicStroke, Color}

import org.jfree.chart.{ChartFactory, ChartUtils}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import pw.byakuren.tbf.markets.StockMarket

object ChartCreator {

  def apply(market: StockMarket): Array[Byte] = { //return an attachment we can add to discord
    val series = new XYSeries(0)
    var i = 0;
    for (v <- market.previousValues) {
      series.add(i, v)
      i+=1
    }
    val dataset = new XYSeriesCollection()
    dataset.addSeries(series)
    val chart = ChartFactory.createXYLineChart(market.name, "Time", "Stock Value", dataset)
    chart.setBackgroundPaint(Color.WHITE)
    chart.setBorderStroke(new BasicStroke(5f))
    ChartUtils.encodeAsPNG(chart.createBufferedImage(800,800))
  }

}
