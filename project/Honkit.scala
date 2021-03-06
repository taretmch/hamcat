import sbt._
import sbt.io.IO
import mdoc.MdocPlugin.autoImport._
import scala.sys.process.Process

object Honkit extends TaskConf {
  val honkitBin = nodeBinDir / cmd("honkit")

  def buildBook = Def.inputTask[Unit] {
    val options = rawStringArg("<honkit command>").parsed
    printRun(Process(s"$honkitBin build $bookJsonDir $honkitBookDir $options"))
  }

  lazy val textBuildHtml = inputKey[Unit]("build Honkit to html")

  private[this] val mdocTask = mdoc.toTask("")

  val settings = Seq(
    textBuildHtml := buildBook.dependsOn(mdocTask).evaluated,
  )
}
