package com.kafka.message.example.other.launch

import java.io.{FileOutputStream, IOException}
import java.nio.ByteBuffer
import java.nio.file.Paths
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import com.kafka.message.example.other.launch.CreateFile._
import com.kafka.message.example.util.{CommandLineHandler, DefaultProperties, PropertyKeys}
import org.apache.commons.cli.{Option, ParseException}

object CreateFile {
  val PathOptionName      = "path"
  val SleepTimeOptionName = "sleepTime"

  /**
    * Gets the directory path default value.
    *
    * @return the directory path default value
    */
  protected def getDirectoryPathDefaultValue: String =
    DefaultProperties.getPropertyValue(PropertyKeys.MailDirectory)

  /**
    * Gets the thread sleep time default value.
    *
    * @return the thread sleep time default value
    */
  protected def getThreadSleepTimeDefaultValue: Int =
    DefaultProperties.getPropertyValue(PropertyKeys.FileCreateThreadSleepTime).toInt

  protected def getProducerOptions: List[Option] =
    List(
      new Option(PathOptionName, PathOptionName, true, "directory path where file is going to be created"),
      new Option(SleepTimeOptionName, SleepTimeOptionName, true, "time difference between two files creation")
    )

  /**
    * The main method.
    *
    * @param args the arguments
    */
  def main(args: Array[String]): Unit =
    try {
      val commandLine                  = new CommandLineHandler(getProducerOptions, args)
      val path: scala.Option[String]   = commandLine.getOption(PathOptionName)
      val sleepTime: scala.Option[Int] = commandLine.getOption(SleepTimeOptionName).map(_.toInt)

      println(s"Init CreateFile - Sleep Time: $sleepTime seconds, path: $path")

      val createFile = new CreateFile(path.getOrElse(getDirectoryPathDefaultValue),
                                      sleepTime.getOrElse(getThreadSleepTimeDefaultValue))
      createFile.createMailContent()

    } catch {
      case e: ParseException       => e.printStackTrace()
      case e: IOException          => e.printStackTrace()
      case e: InterruptedException => e.printStackTrace()
    }
}

/**
  * Instantiates a new creates the file.
  *
  * @param directoryPath the directory path
  * @param threadSleepTime the thread sleep time
  */
class CreateFile(directoryPath: String = getDirectoryPathDefaultValue,
                 threadSleepTime: Int = getThreadSleepTimeDefaultValue) {

  private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

  /**
    * Create mail content.
    */
  private def createMailContent(): Unit =
    while (true) {
      val fileName     = s"File-${format.format(ZonedDateTime.now)}"
      val absolutePath = Paths.get(directoryPath).resolve(fileName).toString

      val fout = new FileOutputStream(absolutePath)
      val fc   = fout.getChannel

      val buffer = ByteBuffer.allocate(1024)
      buffer.put(getStaticFileContent(fileName))
      buffer.flip()

      fc.write(buffer)

      buffer.clear()
      fc.close()
      fout.close()

      println(s"created file - $absolutePath")

      Thread.sleep(threadSleepTime * 1000)
    }

  /**
    * Gets the static file content.
    *
    * @param fileName the file name
    * @return the static file content
    */
  private def getStaticFileContent(fileName: String): Array[Byte] = {
    val content = s"""File Name -  $fileName
                     |The first  line
                     |The second line
                     |The third  line
                     |The fourth line
                     |""".stripMargin

    content.getBytes()
  }
}
