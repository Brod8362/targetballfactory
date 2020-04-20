package pw.byakuren.tbf.http

import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import net.dv8tion.jda.api.JDA
import pw.byakuren.tbf.sql.SQLConnection
import pw.byakuren.tbf.user.EconomyUser
import pw.byakuren.tbf.util.Utility._

import scala.io.Source


class UserRequestHandler(jda: JDA)(implicit sql: SQLConnection) extends HttpHandler {

  private val resStream = this.getClass.getResourceAsStream("/user_page.html")
  private val httpRaw: String = Source.fromInputStream(resStream).getLines().mkString("\n")


  override def handle(httpExchange: HttpExchange): Unit = {
    val raw_params = httpExchange.getRequestURI.toString.split("\\?").drop(1)
    val parameters: Map[String, String] = raw_params.map({ r =>
      val a = r.split("=")
      a(0) -> a(1)
    }).toSeq.toMap
    val id = parameters("id")
    val html = generateHtml(id)
    println(html)
    val resp = html match {
      case Some(data) =>
        (data, 200)
      case None =>
        ("<body><strong>410: Gone</strong></body>", 410)
    }
    httpExchange.sendResponseHeaders(resp._2, resp._1.length)
    httpExchange.getResponseBody.write(resp._1.getBytes())
    httpExchange.getResponseBody.flush()
    httpExchange.getResponseBody.close()
  }

  private def generateHtml(userId: String): Option[String] = {
    Option(jda.getUserById(userId)) match {
      case Some(user) =>
        user.economyUser() match {
          case Some(eu) =>
            var local: String = httpRaw.toString
            val ui: Map[String, String] = userInfo(eu)
            ui.foreachEntry({ (k: String,v: String) =>
              while (local.indexOf(k) > -1) {
                local = local.replace(k, v)
              }
            })
            Some(local)
          case None =>
            None
        }
      case None =>
        None
    }
  }

  private def userInfo(eu: EconomyUser): Map[String, String] = {
    Map(
      "{{user}}" -> eu.user.getName,
      "{{money}}" -> String.format("%.2f", eu.balance.amount),
      "{{networth}}" -> String.format("%.2f", eu.netWorth),
      "{{level}}" -> eu.level.toString,
      "{{xp}}" -> eu.xp.toString,
      "{{xp_next}}" -> eu.totalXpRequired(eu.level + 1).toString,
      "{{energy}}" -> eu.energy.toString,
      "{{max_energy}}" -> eu.maxEnergy.toString
    )
  }
}
