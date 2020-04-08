package pw.byakuren.tbf.inventory

import pw.byakuren.tbf.util.SQLWritable

class Inventory(val a: Array[Item]) extends SQLWritable {

  def this() { this(Array.ofDim[Item](30))}

  def get(i: Int): Option[Item] = Option(a(i))

  def put(item: Item): Unit = a(firstEmptySlot)=item

  def swap(i: Int, i2:Int): Unit = {
    val t = a(i)
    a(i)=a(i2)
    a(i2)=t
  }

  def view(): String = {
    s"[${nonNullItems.mkString(",")}]"
  }

  def nonNullItems: Seq[Item] = a.filter(_!=null)

  def cumulativeItemValue: Double = nonNullItems.map(_.value).sum

  def firstEmptySlot: Int = {
    for (i <- a.indices) {
      if (a(i)==null) return i
    }
    -1
  }

  override def toString: String = {
    nonNullItems.map(_.icon).mkString("Inventory: ", ",", "")
  }

  override def write(): Unit = ???
}
