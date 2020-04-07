package pw.byakuren.tbf.tracking

import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.user.EconomyUser

import scala.collection.mutable

object EconomyUserTracker {

  private val users = new mutable.HashSet[EconomyUser]()

  def apply(u: User): Option[EconomyUser] = {
    users.find{_.user==u} match {
      case Some(x) => Some(x)
      case None => Some(load(u))
    }
  }

  private def load(u: User): EconomyUser = {
    //TODO load from SQL. currently just creates a new blank user
    val x = new EconomyUser(u)
    users.add(x)
    x
  }

  def cumulativeLevel: Int = users.map(_.level).sum

}
