import Dependencies._
import TaskConf._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val core = project
  .settings(name := "hamcat-core")
  .settings(BuildSettings.settings: _*)
  .settings(libraryDependencies ++= scalatest)

lazy val docs = project
  .settings(name := "hamcat-docs")
  .settings(BuildSettings.settings: _*)
  .settings(mdocIn  := mdocInputDir)
  .settings(mdocOut := mdocOutputDir)
  .settings(libraryDependencies ++= cats)
  .enablePlugins(MdocPlugin)
  .settings(Honkit.settings)
  .dependsOn(core)

lazy val root = (project in file("."))
  .settings(name := "root")
  .settings(BuildSettings.settings: _*)
  .settings(publish / skip := true)
  .aggregate(core, docs)
  .dependsOn(core, docs)
