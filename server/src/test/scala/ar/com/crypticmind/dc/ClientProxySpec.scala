package ar.com.crypticmind.dc

import java.net.URL

import org.mockserver.integration.ClientAndServer
import org.mockserver.model.{HttpRequest, HttpResponse}
import org.mockserver.socket.PortFactory
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}
import org.scalatest.{Matchers, WordSpec}

class ClientProxySpec extends WordSpec with Matchers with Eventually {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = scaled(Span(2000, Millis)), interval = scaled(Span(200, Millis)))
  
  private val port = PortFactory.findFreePort
  private val server = ClientAndServer.startClientAndServer(port)
  private val jarBytes = {
    val is = getClass.getResourceAsStream("/dc-client-impl-" + Version.VERSION + ".jar")
    Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
  }

  trait Fixture {
    server.reset()
  }

  object NullLogger extends Logger {
    def debug(message: String, t: Throwable): Unit = ()
    def info(message: String, t: Throwable): Unit = ()
    def warn(message: String, t: Throwable): Unit = ()
    def error(message: String, t: Throwable): Unit = ()
  }

  "The client proxy" should {
    "get a client" in {
      server
        .when(HttpRequest.request.withPath("/dynamic-client/version"))
        .respond(HttpResponse.response(Version.VERSION))
      server
        .when(HttpRequest.request.withPath("/dynamic-client/library"))
        .respond(HttpResponse.response.withBody(jarBytes))
      server
        .when(HttpRequest.request.withPath("/dynamic-client/service/sum/2/3"))
        .respond(HttpResponse.response("5"))

      val cp = new ClientProxy(new URL(s"http://localhost:$port"), NullLogger)

      eventually {
        cp.getClient.isPresent shouldBe true
      }

      cp.getClient.ifPresent(c => c.version() shouldBe Version.VERSION)
      cp.getClient.ifPresent(c => c.sum(2, 3) shouldBe 5)

      server.verify(HttpRequest.request.withPath("/dynamic-client/version"))
      server.verify(HttpRequest.request.withPath("/dynamic-client/library"))
      server.verify(HttpRequest.request.withPath("/dynamic-client/service/sum/2/3"))
    }
  }
}
