import Dependencies._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "scala-category-training",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.typelevel" %% "cats-kernel" % "2.2.0",
      "org.typelevel" %% "cats-core"   % "2.2.0"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
