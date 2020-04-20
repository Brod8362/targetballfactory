package pw.byakuren.tbf.sql

import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.inventory.{Inventory, Item, ItemRegistry}
import pw.byakuren.tbf.markets.StockMarket
import pw.byakuren.tbf.user.{Balance, EconomyUser}
import scalikejdbc._

class SQLConnection {
  Class.forName("org.sqlite.JDBC")
  ConnectionPool.singleton("jdbc:sqlite:tbf3.db", null, null)

  implicit val session: AutoSession.type = AutoSession

  sql"CREATE TABLE IF NOT EXISTS market_values(market INT NOT NULL, value REAL NOT NULL)".execute().apply()
  sql"""CREATE TABLE IF NOT EXISTS inventory(user INT NOT NULL, pos INT NOT NULL, id INT NOT NULL)""".execute().apply()
  sql"""CREATE TABLE IF NOT EXISTS users(id INT NOT NULL, xp INT NOT NULL, money REAL NOT NULL, energy INT NOT NULL, max_energy INT NOT NULL)""".execute().apply()

  def writeMarketValue(market: StockMarket): Unit = {
    sql"INSERT INTO market_values (market, value) VALUES (${market.id}, ${market.value})".execute().apply()
  }

  def getValuesForMarket(market: StockMarket): Seq[Double] = {
    sql"SELECT value FROM market_values WHERE market=${market.id} ORDER BY _rowid_".map(_.double("value")).list().apply()
  }

  def getUserInventory(user: User): Inventory = {
    val buf: Array[Item] = Array.ofDim(Inventory.InventorySize)
    val raw = sql"SELECT * FROM inventory WHERE user=${user.getIdLong}".
      map(rs => (rs.int("pos"), rs.int("id"))).list().apply()
    raw.foreach(t => buf(t._1) = ItemRegistry(t._2).getOrElse(ItemRegistry(0).get))
    new Inventory(buf)
  }

  def writeInventory(eu: EconomyUser): Unit = {
    deleteInventory(eu)
    val inv = eu.inventory.raw
    for (i <- inv.indices) {
      if (inv(i) != null) {
        writeInventoryItem(eu.user.getIdLong, inv(i), i)
      }
    }
  }

  def writeInventoryItem(userId: Long, item: Item, index: Int): Unit = {
    assert(item != null)
    sql"INSERT INTO inventory VALUES ($userId, $index, ${item.id})".executeUpdate().apply()
  }

  def deleteInventory(eu: EconomyUser): Unit = {
    sql"DELETE FROM inventory WHERE user=${eu.user.getId}".execute().apply()
  }

  def getUserInfo(user: User): Option[UserData]  = {
    sql"SELECT * FROM users WHERE id=${user.getIdLong}"
      .map(rs => (rs.int("xp"), rs.double("money"), rs.int("energy"), rs.int("max_energy")))
      .single().apply() match {
      case Some(t) =>
        Some(new UserData(t._1, t._2, t._3, t._4))
      case None =>
        None
    }
  }

  def writeUserInfo(eu: EconomyUser): Unit = {
    sql"INSERT OR REPLACE INTO users VALUES (${eu.user.getIdLong}, ${eu.xp}, ${eu.balance.amount}, ${eu.energy}, ${eu.maxEnergy})".executeUpdate().apply()
  }

  def loadUser(user: User): Option[EconomyUser] = {
    val inv = getUserInventory(user)
    val dataOption = getUserInfo(user)
    dataOption match {
      case Some(data) =>
        Some(new EconomyUser(user, data.xp, new Balance(data.money), inv, data.energy, data.maxEnergy))
      case None =>
        None
    }
  }

  def writeUser(eu: EconomyUser): Unit = {
    writeInventory(eu)
    writeUserInfo(eu)
  }

}
