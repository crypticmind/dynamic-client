import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  organization := "ar.com.crypticmind",
  organizationName := "crypticmind",
  organizationHomepage := Some(url("http://github.com/crypticmind")),
  homepage := Some(url("http://github.com/crypticmind/dynamic-client")),
  startYear := Some(2017),
  scalaVersion := "2.12.2",
  fork in run := true,
  cancelable in run := true,
  javacOptions := Seq(
    "-source", "1.8",
    "-target", "1.8"
  ),
  scalacOptions := Seq(
    "-encoding",
    "utf8",
    "-g:vars",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-target:jvm-1.8",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-Xlog-reflective-calls"
  ),
  exportJars := true
)

lazy val noReleaseSettings = Seq(
  publishArtifact := false,
  publish := (),
  publishLocal := ()
)

lazy val releaseSettings = Seq(
  exportJars := true,
  publishArtifact := false,
  publishArtifact in (Compile, packageBin) := true
)

lazy val server =
  project
    .in(file("server"))
    .enablePlugins(BuildInfoPlugin)
    .settings(commonSettings: _*)
    .settings(releaseSettings: _*)
    .settings(
      description := "Server",
      name := "dc-server",
      mainClass := Some("ar.com.crypticmind.dc.Server"),
      buildInfoKeys := Seq[BuildInfoKey](organization, name, version),
      buildInfoPackage := "ar.com.crypticmind.dc",
      resourceGenerators in Compile += (packageBin in (`client-impl`, Compile)).map(Seq(_)).taskValue,
      libraryDependencies ++= Seq(
        "com.typesafe"                  %   "config"                      % "1.3.0",
        "org.clapper"                   %%  "grizzled-slf4j"              % "1.3.1",
        "org.slf4j"                     %   "slf4j-api"                   % "1.7.21",
        "org.slf4j"                     %   "jcl-over-slf4j"              % "1.7.21",
        "org.apache.logging.log4j"      %   "log4j-to-slf4j"              % "2.7",
        "ch.qos.logback"                %   "logback-classic"             % "1.1.2",
        "com.typesafe.akka"             %%  "akka-http"                   % "10.0.5"
      )
    )

lazy val `client-lib` =
  project
    .in(file("client-lib"))
    .settings(commonSettings: _*)
    .settings(releaseSettings: _*)
    .settings(
      description := "Client library",
      name := "dc-client-lib",
      mainClass := None,
      crossPaths := false,
      autoScalaLibrary := false
    )

lazy val `client-impl` =
  project
    .in(file("client-impl"))
    .dependsOn(`client-lib`)
    .settings(commonSettings: _*)
    .settings(releaseSettings: _*)
    .settings(
      description := "Server-hosted client library implementation",
      name := "dc-client-impl",
      mainClass := None,
      crossPaths := false,
      autoScalaLibrary := false
    )

lazy val stasi =
  project
    .in(file("."))
    .aggregate(server, `client-lib`, `client-impl`)
    .settings(commonSettings: _*)
    .settings(noReleaseSettings: _*)
    .settings(
      mainClass := None
    )
