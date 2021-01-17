import Dependencies._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "1.0.0-SNAPSHOT"
ThisBuild / organization     := "com.criceta"
ThisBuild / organizationName := "taretmch"

lazy val core = project
  .settings(
    name := "hamcat",
    libraryDependencies ++= Seq(
      scalaTest % Test
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)
  )

lazy val docs = project
  .settings(
    mdocIn  := file("docs/src/drafts"),
    mdocOut := file("docs/src/main"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-kernel" % "2.2.0",
      "org.typelevel" %% "cats-core"   % "2.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)
  )
  .dependsOn(core)
  .enablePlugins(MdocPlugin)

lazy val example = project
  .settings(
    name := "hamcat-sample",
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)
  )
  .dependsOn(core)
