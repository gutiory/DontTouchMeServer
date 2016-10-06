import play.sbt.routes.RoutesKeys._

name := """Don't Touch Me Server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  "javax.inject" % "javax.inject" % "1"
)

//routesGenerator := InjectedRoutesGenerator

