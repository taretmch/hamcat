import sbt._

object Dependencies {

  val scalaVersion = "3.4.2"

  val scalatest = Seq(
    "org.scalactic" %% "scalactic" % "3.2.9",
    "org.scalatest" %% "scalatest" % "3.2.9" % "test"
  )
}
