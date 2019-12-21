package pw.byakuren.tbf.markets

class StockMarket(name: String, maxGrowth: Float, maxDecay: Float, iterMean: Int, iterOffset: Int,
                  baseValue: Double, lean: Float, fun: Float, crash: Float, jump: Float) {

  private var value = baseValue

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

  def restoreValue(newValue: Float): Unit = updateValue(newValue)

  def grow(): Unit = updateValue(value*Math.random()*maxGrowth)

  def decay(): Unit = updateValue(-value*Math.random()*maxDecay)

  def skyrocket(): Unit = updateValue(value*Math.random()*jump)

  def crash(): Unit = updateValue(-value*Math.random()*crash)

  private def updateValue(newValue: Double): Unit = {
    val prev = newValue
    value = newValue
    //callback alerting % change and whatnot
  }

  private def trigger(f:Float): Boolean = Math.random() < f

  private def shouldTriggerFun: Boolean = trigger(fun)

  private def fun: Unit = {
    if (!shouldTriggerFun) return
    if (trigger(1-lean)) {
      skyrocket()
    } else {
      crash()
    }
  }

  def iterate: Unit = {
    if (trigger(lean)) {
      grow()
    } else {
      decay()
    }
    val nextWait = iterMean+((Math.random()*iterOffset*2)-iterOffset)
    //schedule next iteration
  }

}
