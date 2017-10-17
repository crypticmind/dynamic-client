
logLevel := Level.Info

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

addSbtPlugin("io.get-coursier" %% "sbt-coursier" % "1.0.0-RC12")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
