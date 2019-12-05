name := "tool"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

lazy val root = (project in file("."))
  .aggregate(
    annealingViewer,
    backwordGenerator,
    core,
    toggler
  )

lazy val annealingViewer = (project in file("annealingViewer"))
  .dependsOn(core)

lazy val backwordGenerator = (project in file("backwordGenerator"))
  .dependsOn(core)

lazy val core = project in file("core")

lazy val toggler = (project in file("toggler"))
  .dependsOn(core)
