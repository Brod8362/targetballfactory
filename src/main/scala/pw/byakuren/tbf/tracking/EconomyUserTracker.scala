package pw.byakuren.tbf.tracking

import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.sql.{SQLConnection, SQLWritable}
import pw.byakuren.tbf.user.EconomyUser

import scala.collection.mutable

object EconomyUserTracker extends SQLWritable{

  private val users = new mutable.HashSet[EconomyUser]()

  def apply(u: User)(implicit sql: SQLConnection): Option[EconomyUser] = {
    users.find {
      _.user == u
    } match {
      case Some(x) => Some(x)
      case None => Some(load(u))
    }
  }

  private def load(u: User)(implicit sql: SQLConnection): EconomyUser = {
    val eu = sql.loadUser(u) match {
      case Some(x) =>
        x
      case None =>
        new EconomyUser(u)
    }
    users.add(eu)
    eu
  }

  def cumulativeLevel: Int = users.map(_.level).sum

  override def write()(implicit sql: SQLConnection): Unit = {
    users.foreach(_.write())
  }
}
