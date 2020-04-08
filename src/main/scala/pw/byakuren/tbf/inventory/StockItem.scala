package pw.byakuren.tbf.inventory

import pw.byakuren.tbf.markets.StockMarket

class StockItem(market: StockMarket) extends Item {

  override var id: Int = -1*market.id

  override def name: String = s"Stock Certificate: ${market.name}"

  override def icon: String = s"\uD83D\uDCB8:${market.id}"

  override def value: Double = market.value

  override def meta: Int = 0
}
