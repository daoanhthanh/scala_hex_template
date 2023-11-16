package vn.ventures.domain.commons

import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime

import java.io.{File, FilenameFilter}
import java.nio.file.{Files => JFiles, StandardCopyOption}
import zio.ZIO

import scala.util.Try

object CommonService {}

//  def getImageByteArray(fileName: String, filePath: String, defaultFilePath: Option[String] = None): Array[Byte] = {
//    val source = scala.io.Source.fromFile(getFileFromPath(fileName, filePath, defaultFilePath))(scala.io.Codec.ISO8859)
//    val byteArray = source.map(_.toByte).toArray
//    source.close()
//    byteArray
//  }

//  def getFileFromPath(fileName: String, filePath: String, defaultFilePath: Option[String] = None): File = {
//    val rootLocal = configuration.get[String]("project.local")
//
//    val fileDefaultPath: String = "%s/%s".format(rootLocal, defaultFilePath.getOrElse("/data/images/notFound.jpg"))
//
//    val fullFilePath: String = "%s/%s/%s".format(rootLocal, filePath, fileName)
//
//    val file = new File(fullFilePath)
//
//    if (file.exists()) {
//      file
//    } else {
//      new File(fileDefaultPath)
//    }
//  }
//
//  def createNewFolder(path: String, folderName: String): Boolean = {
//    val dir = new File(path + folderName)
//    dir.mkdirs()
//  }
//
//  def deleteFolderAndChildren(path: String): Boolean = {
//    if (new File(path).exists()) {
//      val folder = new Directory(new File(path, "/"))
//      folder.deleteRecursively()
//      true
//    } else {
//      false
//    }
//  }
//
//  def copyImageWithMode(src: String, dst: String): String = {
//    val command = s"install -m 664 $src $dst"
//    ExecuteCommand.run(command)
//    dst
//  }
//
//  def copy(from: String, to: String): Boolean =
//    Try {
//      val cpCommand = s"cp -rf $from $to"
//      ExecuteCommand.run(cpCommand)
//      true
//    }.getOrElse {
//      logger.error("Could not copy Path: " + from + " to: " + to)
//      false
//    }
//
//  def mv(from: String, to: String): Boolean =
//    Try {
//      val cpCommand = s"mv $from $to"
//      ExecuteCommand.run(cpCommand)
//      true
//    }.getOrElse {
//      logger.error("Could not copy Path: " + from + " to: " + to)
//      false
//    }
//
//  def mv(oldName: String, newName: String, replace: Boolean = false): Boolean = {
//    Try {
//      if (replace) JFiles.move(new File(oldName).toPath, new File(newName).toPath, StandardCopyOption.REPLACE_EXISTING)
//      else new File(oldName).renameTo(new File(newName))
//      true
//    }.getOrElse {
//      logger.error("Could not move file: " + oldName + " to " + newName)
//      false
//    }
//  }
//
//  def deleteFileInPath(path: String): Boolean = Try {
//    for {
//      files <- Option(new File(path).listFiles)
//      file  <- files
//    } file.delete()
//    true
//  }.getOrElse {
//    logger.error("Could not delete file in path: " + path)
//    false
//  }
//
//  def getSubfolders(path: String): Array[File] = {
//    val dir = new File(path)
//    dir.listFiles(new FilenameFilter {
//      override def accept(dir: File, name: String): Boolean = new File(dir, name).isDirectory
//    })
//  }
//
//  /** @param hour
//    * @param minute
//    * @return
//    *   check time of startTime has input hour, minute is before now? if false next intervalMinutes and check again
//    */
//  def nextExecutionTime(startTime: DateTime, hour: Int, minute: Int, intervalMinutes: Int): DateTime = {
//    val nextTime: DateTime =
//      new DateTime(startTime.getYear, startTime.getMonthOfYear, startTime.getDayOfMonth, hour, minute)
//    if (nextTime.isBeforeNow) {
//      val tmp = nextTime.plusMinutes(intervalMinutes)
//      nextExecutionTime(tmp, tmp.getHourOfDay, tmp.getMinuteOfHour, intervalMinutes)
//    } else {
//      nextTime
//    }
//  }
