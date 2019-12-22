package pw.byakuren.tbf.user

import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.actions.Action
import pw.byakuren.tbf.inventory.Item

class EconomyUser(val user: User, var xp: Int, val balance: Balance, var inventory: Array[Item]) {

  def level:Int = 0 //XP formula goes here

  def perform(a: Action) {
    if (a.perform()) xp += a.xpYield
  }

}
