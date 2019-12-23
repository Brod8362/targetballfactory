package pw.byakuren.tbf.inventory

import pw.byakuren.tbf.util.SQLWritable

trait Item extends SQLWritable {

  def id: Int
  def name: String
  def icon: String
  def value: Double

}
