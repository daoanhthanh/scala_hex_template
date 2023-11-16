import sbt.Keys.version
import sbt.{Command, Project}

import scala.language.postfixOps
import scala.sys.process.*
import scala.util.Try

object SbtCommands {
  private val ASCII_Text: String => String = (version: String) =>
    s"""
       |
       |     ██████╗ ███████╗███████╗██╗ ██████╗ ███╗   ██╗    ████████╗ ██████╗  ██████╗ ██╗
       |     ██╔══██╗██╔════╝██╔════╝██║██╔════╝ ████╗  ██║    ╚══██╔══╝██╔═══██╗██╔═══██╗██║
       |     ██║  ██║█████╗  ███████╗██║██║  ███╗██╔██╗ ██║       ██║   ██║   ██║██║   ██║██║
       |     ██║  ██║██╔══╝  ╚════██║██║██║   ██║██║╚██╗██║       ██║   ██║   ██║██║   ██║██║
       |     ██████╔╝███████╗███████║██║╚██████╔╝██║ ╚████║       ██║   ╚██████╔╝╚██████╔╝███████╗
       |     ╚═════╝ ╚══════╝╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═══╝       ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝ v$version
       |""".stripMargin

  def greet: Command = Command.command("greet") { state =>
    val margin = 5

    val extracted      = Project.extract(state)
    val projectVersion = extracted.get(version)

    val currentGitUser = Try("git config user.name" !!).getOrElse("My Friend")

    val maxLengthLine = ASCII_Text(projectVersion).linesIterator.toSeq.map(_.length).max - margin

    val greeting = s"Hello ${currentGitUser.trim}!"

    val nameLength      = greeting.length
    val leftDashLength  = (maxLengthLine - nameLength) / 2 - 1
    val rightDashLength = maxLengthLine - nameLength - leftDashLength - 1

    val helloText = s"${" " * margin}${"-" * leftDashLength} $greeting ${"-" * rightDashLength}"

    println(ASCII_Text(projectVersion))
    println(helloText)
    print("\n")

    state
  }

}
