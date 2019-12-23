package pw.byakuren.tbf.inventory

class Inventory(val a: Array[Item]) {

  def this() { this(Array.ofDim[Item](100))}

  def get(i: Int): Option[Item] = Option(a(i))

  def put(i: Int, item: Item): Unit = a(i)=item

  def swap(i: Int, i2:Int): Unit = {
    val t = a(i)
    a(i)=a(i2)
    a(i2)=t
  }

  def view(): String = {
    s"[${a.mkString(",")}]"
  }

}
