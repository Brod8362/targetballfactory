package pw.byakuren.tbf.markets

class StockMarketThreadManager {

  var threads: Option[Seq[Thread]] = None

  def start(markets: Seq[StockMarket]): Unit = {
    threads = Some(markets.map(m => {
      new Thread(() => {
        while (true) {
          val wait = m.iterate()
          try {
            Thread.sleep(wait)
          } catch {
            case _:InterruptedException =>
              return
          }
        }
      })
    }))
    threads.foreach(_.foreach(_.start()))
  }

  def stop(): Unit = {
    threads.foreach(_.foreach(_.interrupt()))
  }

}
