package pw.byakuren.tbf.user

class Balance(private var bal: Double) {

  def deposit(amt: Double): Unit = {
    if (amt > 0) bal += amt
  }

  def withdraw(amt: Double): Boolean = {
    if (amt <= bal) {
      bal-=amt
      return true
    }
    false
  }

  def amount: Double = bal

}
