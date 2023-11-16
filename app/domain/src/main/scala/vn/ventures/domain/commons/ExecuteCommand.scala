package vn.ventures.domain.commons

object ExecuteCommand {
  def reader(proc: Process): String = {
    val streamReader = new java.io.InputStreamReader(proc.getInputStream)
    val bufferedReader = new java.io.BufferedReader(streamReader)
    val stringBuilder = new java.lang.StringBuilder()
    var line: String = ""
    while ({
      line = bufferedReader.readLine
      line != null
    }) {
      stringBuilder.append(line)
      stringBuilder.append("\n")
    }
    bufferedReader.close()

    stringBuilder.toString.stripLineEnd
  }

  /**
   *
   * @param command cmd wait run
   * @return string of result value
   */
  def run(command: String): String = {
    val args = command.split(" ")
    val processBuilder = new ProcessBuilder(args: _*)
    processBuilder.redirectErrorStream(true)
    val proc = processBuilder.start()

    reader(proc)
  }
}
