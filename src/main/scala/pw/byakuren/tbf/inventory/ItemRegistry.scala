package pw.byakuren.tbf.inventory

import pw.byakuren.tbf.markets.StockMarket

object ItemRegistry {

  private var itemMap: Map[Int, Item] = Map()
  private var nextItemId = 0
  register(NullItem)

  def register(item: Item): Int = {
    itemMap=itemMap++Map((nextItemId, item))
    item.id=nextItemId
    nextItemId+=1
    nextItemId-1
  }

  def apply(i: Int): Option[Item] = {
    itemMap.get(i)
  }

  def generateStockItems(markets: Seq[StockMarket]): Unit = {
    for (market <- markets) {
      val si = new StockItem(market)
      println("Market Stock added: "+si)
      market.stockItem = Some(si)
      itemMap=itemMap++Map((si.id, si))
    }
  }

}
