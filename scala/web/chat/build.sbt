name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.12.6", "2.11.12")

libraryDependencies += play.sbt.PlayImport.cacheApi

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.github.karelcemus" %% "play-redis" % "2.3.0"
libraryDependencies += jdbc
libraryDependencies += "org.playframework.anorm" %% "anorm-akka" % "2.6.0"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1200-jdbc41"
libraryDependencies += "org.mockito" % "mockito-core" % "2.23.4" % Test
