package pw.byakuren.tbf.actions
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.user.EconomyUser

class BallsMadeAction(m: Message, ballsMade: Int) extends Action {
  override val xpYield: Int = ballsMade
  override val energyCost: Int = -1

  override def perform(u: EconomyUser): Boolean = true

  override def context: Message = m

  override def minimumLevel: Int = 0
}
