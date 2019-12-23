package pw.byakuren.tbf.util

import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.inventory.Item
import pw.byakuren.tbf.tracking.EconomyUserTracker
import pw.byakuren.tbf.user.{Balance, EconomyUser}

object Utility {

  implicit class getEconomyUserFromUser(u: User) {
    def economyUser(): Option[EconomyUser] = {
      //TODO code from DB here
      EconomyUserTracker.get(u)
    }
  }

  def minimum(min: Int, v: Int): Int = if (v < min) min else v

}
