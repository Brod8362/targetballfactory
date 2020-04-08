package pw.byakuren.tbf.actions

import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.user.EconomyUser

trait Action {

  val xpYield: Int

  val energyCost: Int

  def perform(u: EconomyUser): Boolean

  def context: Message

  def minimumLevel: Int


}
