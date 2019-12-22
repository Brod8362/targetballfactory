package pw.byakuren.tbf.util

import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.inventory.Item
import pw.byakuren.tbf.user.{Balance, EconomyUser}

object Utility {

  implicit class getEconomyUserFromUser(u: User) {
    def economyUser(): Option[EconomyUser] = {
      //TODO code from DB here
      Some(new EconomyUser(u, 0, new Balance(0), Array.ofDim[Item](100)))
    }
  }

}