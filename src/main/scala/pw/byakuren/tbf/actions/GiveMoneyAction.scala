package pw.byakuren.tbf.actions
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.user.EconomyUser

class GiveMoneyAction(message: Message, amount: Int) extends Action {
  override val xpYield: Int = 0
  override val energyCost: Int = 1

  override def perform(u: EconomyUser): Boolean = {
    u.balance.deposit(amount)
    true
  }

  override def context: Message = message

  override def minimumLevel: Int = 0
}
