
logLevel := Level.Info

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

addSbtPlugin("io.get-coursier" %% "sbt-coursier" % "1.0.0-M14")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")
