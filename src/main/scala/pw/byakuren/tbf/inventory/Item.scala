package pw.byakuren.tbf.inventory

trait Item {

  var id: Int
  def name: String
  def icon: String
  def value: Double
  def meta: Int

  override def toString = s"Item($id, $name, $icon, $value, $meta)"
}
