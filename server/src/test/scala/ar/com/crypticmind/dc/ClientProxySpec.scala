package ar.com.crypticmind.dc

import java.net.URL

import ar.com.crypticmind.dc.clientlib.ClientProxy
import ar.com.crypticmind.dc.clientlib.logging.{Logger, Slf4jLogger}
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
  private val version = ar.com.crypticmind.dc.clientimpl.Version.VERSION
  private val jarBytes = {
    val is = getClass.getResourceAsStream("/dc-client-impl-" + version + ".jar")
    Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
  }

  trait Fixture {
    server.reset()
  }

  "The client proxy" should {
    "get a client" in {
      server
        .when(HttpRequest.request.withPath("/dynamic-client/version"))
        .respond(HttpResponse.response(version))
      server
        .when(HttpRequest.request.withPath("/dynamic-client/library"))
        .respond(HttpResponse.response.withBody(jarBytes))
      server
        .when(HttpRequest.request.withPath("/dynamic-client/service/sum/2/3"))
        .respond(HttpResponse.response("5"))

      val cp = new ClientProxy(new URL(s"http://localhost:$port"), new Slf4jLogger)

      eventually {
        Option(cp.getClient) shouldBe defined
      }

      Option(cp.getClient).map(c => c.version()) should contain (version)
      Option(cp.getClient).map(c => c.sum(2, 3)) should contain (5)

      server.verify(HttpRequest.request.withPath("/dynamic-client/version"))
      server.verify(HttpRequest.request.withPath("/dynamic-client/library"))
      server.verify(HttpRequest.request.withPath("/dynamic-client/service/sum/2/3"))
    }
  }
}
