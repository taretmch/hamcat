import Dependencies._
import TaskConf._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "1.0.0-SNAPSHOT"
ThisBuild / organization     := "com.criceta"
ThisBuild / organizationName := "taretmch"

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint:-unused,_",
    "-Ywarn-dead-code",
    "-Ywarn-unused:imports"
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)
)

lazy val core = project
  .settings(name := "hamcat-core")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    scalaTest % Test
  ))

lazy val docs = project
  .settings(name := "hamcat-docs")
  .settings(commonSettings: _*)
  .settings(mdocIn  := mdocInputDir)
  .settings(mdocOut := mdocOutputDir)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-kernel" % "2.2.0",
    "org.typelevel" %% "cats-core"   % "2.2.0"
  ))
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
