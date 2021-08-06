import Dependencies._
import TaskConf._

ThisBuild / scalaVersion     := "3.0.1"
ThisBuild / crossScalaVersions ++= Seq("2.13.3", "3.0.1")
ThisBuild / version          := "1.0.0-SNAPSHOT"
ThisBuild / organization     := "com.criceta"
ThisBuild / organizationName := "taretmch"

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:implicitConversions",
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq(
      "-unchecked",
      "-source:3.0-migration",
      "-Ykind-projector"
    )
    case _ => Seq(
      "-deprecation",
      "-Xfatal-warnings",
    )
  }),
  libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
  //addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)
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
    "org.typelevel" %% "cats-kernel" % "2.6.1",
    "org.typelevel" %% "cats-core"   % "2.6.1"
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
