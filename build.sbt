import Dependencies._
import TaskConf._

ThisBuild / scalaVersion := "3.2.0"

val commonSettings = Seq(
  scalaVersion     := "3.2.0",
  version          := "0.1.0",
  organization     := "com.criceta",
  organizationName := "taretmch",
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Ykind-projector"
  )
)

lazy val core = project
  .settings(name := "hamcat-core")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= scalatest)

lazy val docs = project
  .settings(name := "hamcat-docs")
  .settings(commonSettings: _*)
  .settings(mdocIn  := mdocInputDir)
  .settings(mdocOut := mdocOutputDir)
  .settings(libraryDependencies ++= cats)
  .enablePlugins(MdocPlugin)
  .settings(Honkit.settings)
  .dependsOn(core)

lazy val example = project
  .settings(name := "hamcat-sample")
  .dependsOn(core)

lazy val root = (project in file("."))
  .settings(name := "hamcat")
  .settings(commonSettings: _*)
  .aggregate(core)
  .dependsOn(core)
