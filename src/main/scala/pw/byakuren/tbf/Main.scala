package pw.byakuren.tbf

import java.io.{File, FileInputStream, InputStream}

import org.yaml.snakeyaml.Yaml
import pw.byakuren.tbf.http.HttpThread
import pw.byakuren.tbf.markets.StockMarket
import pw.byakuren.tbf.sql.SQLConnection

import scala.jdk.CollectionConverters._
import scala.collection.mutable

object Main {

  def main(args: Array[String]): Unit = {
    val yaml = new Yaml()
    val dir = System.getProperty("user.dir")
    val input:InputStream = new FileInputStream(new File(dir+"/config.yaml"))
    val output:java.util.LinkedHashMap[String,AnyRef] = yaml.load(input)
    implicit val sql: SQLConnection = new SQLConnection()

    val markets_raw: mutable.Map[String,AnyRef] = output.get("markets") match {
      case map: java.util.Map[String,AnyRef] => map.asScala
      case null => return//markets not in config, throw exception
      case _ => return// something not good
    }
    val markets = parseMarkets(markets_raw)
    val stockChannel = output.get("stock-alert-channel").asInstanceOf[Long]
    val ballChannel = output.get("ball-alert-channel").asInstanceOf[Long]
    val loopChannel = output.get("voice-loop-channel").asInstanceOf[Long]
    val loopURL = output.get("loop-url").asInstanceOf[String]
    val bot = new EconomyBot(output.get("token").toString, ".", markets, stockChannel, ballChannel, loopChannel, loopURL)
    val httpServer = new HttpThread(bot.jda)
  }

  def parseMarkets(markets: mutable.Map[String,AnyRef])(implicit SQLConnection: SQLConnection): Array[StockMarket] = {
    var nextMarketId = 0
    for (name <- markets.keys) yield {
      val dict: mutable.Map[String,AnyRef] = markets.get(name) match {
        case map:Some[java.util.LinkedHashMap[String,AnyRef]] => map.value.asScala
        case None => throw new RuntimeException("no markets in configuration")
        case _ => throw new RuntimeException("unknown error parsing StockMarkets") // something not good
      }
      nextMarketId+=1
      val maxGrowth:Float = dict("max-growth").asInstanceOf[Double].toFloat
      val maxDecay: Float = dict("max-decay").asInstanceOf[Double].toFloat
      val iterMean: Int = dict("iter-mean").asInstanceOf[Int]
      val iterOffset: Int = dict("iter-offset").asInstanceOf[Int]
      val baseValue: Double = dict("base-value").asInstanceOf[Double]
      val lean: Float = dict("lean").asInstanceOf[Double].toFloat
      val fun: Float = dict("fun").asInstanceOf[Double].toFloat
      val crash: Float = dict("crash").asInstanceOf[Double].toFloat
      val jump: Float = dict("jump").asInstanceOf[Double].toFloat
      val sm = new StockMarket(name, nextMarketId, maxGrowth, maxDecay, iterMean, iterOffset, baseValue, lean, fun, crash, jump)
      val pv = SQLConnection.getValuesForMarket(sm)
      if (pv.nonEmpty)
        sm.restoreValues(pv)
      sm
    }
  }.toArray

}
