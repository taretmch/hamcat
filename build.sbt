import Dependencies._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.taretmch"
ThisBuild / organizationName := "taretmch"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)

lazy val root = (project in file("."))
  .settings(
    name := "scala-category-training",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.typelevel" %% "cats-kernel" % "2.2.0",
      "org.typelevel" %% "cats-core"   % "2.2.0"
    )
  )

lazy val docs = (project in file("docs"))
  .settings(
    mdocIn  := file("docs/src/drafts"),
    mdocOut := file("docs/src/main")
  )
  .dependsOn(root)
  .enablePlugins(MdocPlugin)

