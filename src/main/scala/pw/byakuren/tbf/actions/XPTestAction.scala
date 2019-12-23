package pw.byakuren.tbf.actions
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.user.EconomyUser

class XPTestAction(m: Message, a: Int) extends Action {

  override val xpYield: Int = a

  override def perform(u: EconomyUser): Boolean = true

  override def context: Message = m

  override def minimumLevel: Int = 0

}
