package pw.byakuren.tbf.actions
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.inventory.{Item, ItemRegistry, NullItem, RegularItem, StockItem}
import pw.byakuren.tbf.user.EconomyUser

class BuyStockAction(message: Message, stockId: Int) extends Action {
  override val xpYield: Int = 1
  override val energyCost: Int = 1

  override def perform(u: EconomyUser): Boolean = {
    val stockItem = ItemRegistry(-1*stockId)
    println(stockItem)
    if (!stockItem.getOrElse(NullItem).isInstanceOf[StockItem]) {
      message.getChannel.sendMessage("Invalid market ID.").queue()
      return false
    }
    if (u.balance.amount >= stockItem.get.value) {
      u.balance.withdraw(stockItem.get.value)
      u.inventory.put(stockItem.get)
    } else {
      message.getChannel.sendMessage("Insufficient funds.").queue()
      return false
    }
    true
  }

  override def context: Message = message

  override def minimumLevel: Int = 3
}
