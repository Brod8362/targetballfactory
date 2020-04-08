package pw.byakuren.tbf.inventory

import pw.byakuren.tbf.util.SQLWritable

trait Item extends SQLWritable {

  var id: Int
  def name: String
  def icon: String
  def value: Double
  def meta: Int

  final override def write(): Unit = {
    //do whatever
  }

  override def toString = s"Item($id, $name, $icon, $value, $meta)"
}
