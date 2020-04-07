package pw.byakuren.tbf.inventory

import pw.byakuren.tbf.util.SQLWritable

class Inventory(val a: Array[Item]) extends SQLWritable {

  private var next = 0

  def this() { this(Array.ofDim[Item](100))}

  def get(i: Int): Option[Item] = Option(a(i))

  def put(item: Item): Unit = a(next)=item; next+=1

  def swap(i: Int, i2:Int): Unit = {
    val t = a(i)
    a(i)=a(i2)
    a(i2)=t
  }

  def view(): String = {
    s"[${a.mkString(",")}]"
  }

  override def write(): Unit = ???
}
