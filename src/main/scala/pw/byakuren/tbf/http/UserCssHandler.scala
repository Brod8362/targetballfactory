package pw.byakuren.tbf.http

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

class UserCssHandler extends HttpHandler {

  private val css = this.getClass.getResourceAsStream("/user_page.css").readAllBytes()

  override def handle(httpExchange: HttpExchange): Unit = {
    httpExchange.sendResponseHeaders(200, css.size)
    httpExchange.getResponseBody.write(css)
    httpExchange.getResponseBody.flush()
    httpExchange.getResponseBody.close()
  }
}
