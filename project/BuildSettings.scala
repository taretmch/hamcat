import sbt.*
import sbt.Keys.*

object BuildSettings {

  val settings = Seq(
    organization     := "com.github.taretmch",
    organizationName := "taretmch",
    scalaVersion     := Dependencies.scalaVersion,
    version          := "0.1.1-beta.0",
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
