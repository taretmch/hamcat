import sbt._

object Dependencies {

  val catsVersion = "2.6.1"

  val scalatest = Seq(
    "org.scalactic" %% "scalactic" % "3.2.9",
    "org.scalatest" %% "scalatest" % "3.2.9" % "test"
  )

  val cats = Seq(
    "cats-kernel", "cats-core"
  ).map("org.typelevel" %% _ % catsVersion)
}
