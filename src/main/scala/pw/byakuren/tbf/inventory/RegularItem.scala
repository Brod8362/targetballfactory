package pw.byakuren.tbf.inventory

class RegularItem(namea: String, icona: String, valuea: Double, metaa: Int) extends Item {


  override def name: String = namea

  override def icon: String = icona

  override def value: Double = valuea

  override def meta: Int = metaa

  override var id: Int = _
}
