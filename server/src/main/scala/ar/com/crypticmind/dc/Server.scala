package ar.com.crypticmind.dc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.headers.ContentDispositionTypes.attachment
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Try

object Server extends App {

  implicit private val system: ActorSystem = ActorSystem("stasi")
  implicit private val materializer: ActorMaterializer = ActorMaterializer()
  implicit private val ec: ExecutionContext = system.dispatcher

  private val implJar = s"dc-client-impl-${Version.VERSION}.jar"

  private val route =
    pathPrefix("dynamic-client") {
      path("library") {
        respondWithHeader(`Content-Disposition`(attachment, Map("filename" -> implJar))) {
          getFromResource(implJar, ContentTypes.`application/octet-stream`, getClass.getClassLoader)
        }
      } ~
      path("version") {
        get {
          complete(Version.VERSION)
        }
      } ~
      path("service" / "sum" / IntNumber / IntNumber) { (a, b) =>
        get {
          complete(String.valueOf(a + b))
        }
      }
    }

  private val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

  sys.addShutdownHook {
    val closing = bindingFuture.flatMap(_.unbind())
    Try(Await.ready(closing, 5.seconds))
  }

}
