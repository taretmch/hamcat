import Dependencies._
import TaskConf._

val isDotty = Def.setting(
  CrossVersion.partialVersion(scalaVersion.value).exists(_._1 == 3)
)
val Scala213 = "2.13.3"
val Scala3   = "3.0.1"

ThisBuild / crossScalaVersions := Seq(Scala213, Scala3)
ThisBuild / scalaVersion       := Scala213
ThisBuild / version            := "1.0.0-SNAPSHOT"
ThisBuild / organization       := "com.criceta"
ThisBuild / organizationName   := "taretmch"

val kindProjectorVersion = "0.11.1"
val catsVersion          = "2.6.1"

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:implicitConversions",
  ) ++ (if (isDotty.value) {
    Seq(
      "-unchecked",
      "-source:3.0-migration",
      "-Ykind-projector"
    )
  } else {
    Seq(
      "-deprecation",
      "-Xfatal-warnings"
    )
  }),
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.2.9",
    "org.scalatest" %% "scalatest" % "3.2.9" % "test"
  ) ++ (if (isDotty.value) Nil else Seq(
    compilerPlugin(("org.typelevel" %% "kind-projector" % kindProjectorVersion).cross(CrossVersion.full))
  ))
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
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-core"   % catsVersion
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
