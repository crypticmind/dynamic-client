package ar.com.crypticmind.dc

import scala.util.Try

object Server extends App {

  val jar = s"dc-client-impl-${BuildInfo.version}.jar"

  println(s"JAR: $jar --> " + Try(Option(getClass.getClassLoader.getResourceAsStream(jar)).get))

}
