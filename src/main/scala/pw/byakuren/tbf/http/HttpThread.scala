package pw.byakuren.tbf.http

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import com.sun.net.httpserver.HttpServer
import net.dv8tion.jda.api.JDA
import pw.byakuren.tbf.sql.SQLConnection

class HttpThread(jda: JDA)(implicit sql: SQLConnection) {

  private val httpServer: HttpServer = HttpServer.create(new InetSocketAddress("localhost",8001),0)
  private val threadPoolExecutor = Executors.newFixedThreadPool(10)

  httpServer.createContext("/test", new TestHandler)
  httpServer.createContext("/user", new UserRequestHandler(jda: JDA))
  httpServer.createContext("/res/user_page.css", new UserCssHandler)
  httpServer.setExecutor(threadPoolExecutor)
  httpServer.start()

}
