import sbt.*
import sbt.Keys.*

object BuildSettings {

  val settings = Seq(
    organization     := "com.github.taretmch",
    organizationName := "taretmch",
    scalaVersion     := Dependencies.scala3,
    version          := "0.2.0-beta.0",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-Wunused:implicits",
      "-Wunused:explicits",
      "-Wunused:imports",
      "-Wunused:locals",
      "-Wunused:params",
      "-Wunused:privates",
      "-Wvalue-discard"
    )
  )
}
