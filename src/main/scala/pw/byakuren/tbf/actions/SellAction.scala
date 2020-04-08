package pw.byakuren.tbf.actions
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.user.EconomyUser

class SellAction(message: Message, index: Int) extends Action {
  override val xpYield: Int = 1
  override val energyCost: Int = 0

  override def perform(u: EconomyUser): Boolean = {
    val x = u.inventory.a(index)
    if (x == null) {
      message.getChannel.sendMessage("invalid item index, check with dinv command").queue()
      false
    } else {
      u.inventory.a(index) = null
      u.balance.deposit(x.value)
      message.getChannel.sendMessage(s"Sold ${x.name} for $$${x.value}").queue()
      true
    }
  }

  override def context: Message = ???

  override def minimumLevel: Int = ???
}
