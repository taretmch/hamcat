import sbt.*
import sbt.io.IO

import mdoc.MdocPlugin.autoImport.*

import scala.sys.process.Process

/** Honkit task configuration */
object Honkit extends TaskConf {

  /** Honkit command */
  val honkitBin = nodeBinDir / cmd("honkit")

  /** Input Task: Build honkit with arguments */
  def buildBook = Def.inputTask[Unit] {
    val options = rawStringArg("<honkit command>").parsed
    printRun(Process(s"$honkitBin build $bookJsonDir $honkitBookDir $options"))
  }

  /** Task: Build honkit to html */
  lazy val textBuildHtml = inputKey[Unit]("build Honkit to html")

  private[this] val mdocTask = mdoc.toTask("")

  val settings = Seq(
    textBuildHtml := buildBook.dependsOn(mdocTask).evaluated,
  )
}
