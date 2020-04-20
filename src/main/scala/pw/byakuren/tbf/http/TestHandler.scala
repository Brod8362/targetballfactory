package pw.byakuren.tbf.http

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

class TestHandler extends HttpHandler{
  override def handle(httpExchange: HttpExchange): Unit = {
    val resp = "<body>hello</body>"
    httpExchange.sendResponseHeaders(200, resp.length)
    httpExchange.getResponseBody.write(resp.getBytes())
    httpExchange.getResponseBody.flush()
    httpExchange.getResponseBody.close()
  }
}
