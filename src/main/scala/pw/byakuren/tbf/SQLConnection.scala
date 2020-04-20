package pw.byakuren.tbf

import pw.byakuren.tbf.markets.StockMarket
import scalikejdbc._

class SQLConnection {
  Class.forName("org.sqlite.JDBC")
  ConnectionPool.singleton("jdbc:sqlite:tbf3.db", null, null)

  implicit val session: AutoSession.type = AutoSession

  sql"CREATE TABLE IF NOT EXISTS market_values(market INT NOT NULL, value REAL NOT NULL)".execute().apply()

  def writeMarketValue(market: StockMarket): Unit = {
    sql"INSERT INTO market_values (market, value) VALUES (${market.id}, ${market.value})".execute().apply()
  }

  def getValuesForMarket(market: StockMarket): Seq[Double] = {
    sql"SELECT value FROM market_values WHERE market=${market.id} ORDER BY _rowid_".map(_.double("value")).list().apply()
  }

}
