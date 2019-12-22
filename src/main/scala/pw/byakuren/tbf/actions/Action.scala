package pw.byakuren.tbf.actions

trait Action {

  val xpYield: Int

  def perform(): Boolean


}
