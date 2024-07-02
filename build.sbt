import Dependencies.*

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val core = project
  .settings(name := "hamcat-core")
  .settings(BuildSettings.settings: _*)
  .settings(libraryDependencies += munit)

lazy val docs = project
  .settings(name := "hamcat-docs")
  .settings(BuildSettings.settings: _*)
  .settings(mdocIn  := file("docs"))
  .settings(mdocOut := file("mdoc-output"))
  .enablePlugins(MdocPlugin)
  .dependsOn(core)

lazy val root = (project in file("."))
  .settings(name := "root")
  .settings(BuildSettings.settings: _*)
  .settings(publish / skip := true)
  .aggregate(core, docs)
  .dependsOn(core, docs)
