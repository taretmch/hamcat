import sbt._
import sbt.complete.Parser
import scala.util.Properties
import scala.sys.process._

object TaskConf extends TaskConf

/** Configuration for sbt tasks */
trait TaskConf {

  // Directory for Honkit binary
  val nodeBinDir      = file("node_modules/.bin/")

  // Directory in which book.json is
  val honkitConfDir   = file(".")

  // Workspace of honkit
  val honkitBookDir   = file("honkit/docs")

  // Output directory of mdoc
  val mdocOutputDir   = file("docs/target/mdoc")
  val mdocInputDir    = file("docs/src/main")

  protected def rawStringArg(argLabel: String = "<arg>"): Parser[String] = {
    Def.spaceDelimited(argLabel).map(_.mkString(" "))
  }

  def printRun(p: ProcessBuilder) : Unit = {
    p.lineStream foreach println
  }

  def cmd(name: String) =
    if(Properties.isWin) s"${name}.cmd" else name
}
