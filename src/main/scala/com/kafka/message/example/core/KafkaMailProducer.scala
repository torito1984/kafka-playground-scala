package com.kafka.message.example.core

import java.io.{File, FileFilter, IOException, RandomAccessFile}
import java.nio.channels.FileChannel
import java.nio.file.LinkOption.NOFOLLOW_LINKS
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file._
import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.collection.JavaConverters._
import scala.util.Random

/**
  * Instantiates a new kafka producer.
  *
  * @param topic         the topic
  * @param directoryPath the directory path
  */
class KafkaMailProducer(topic: String, directoryPath: String, port: Int = KafkaMailProperties.kafkaServerPort)
    extends Thread {

  val props = new Properties()
  props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("partitioner.class", "com.kafka.message.example.adapt.CustomPartitioner")
  props.put("bootstrap.servers", KafkaMailProperties.kafkaServerURL + ":" + port)
  val producer = new KafkaProducer[Integer, String](props)
  val rnd      = new Random()

  override def run(): Unit = {
    val dir = Paths.get(directoryPath)
    try new WatchDir(dir).start()
    catch {
      case ex: IOException => ex.printStackTrace()
    }
  }

  private class DemoCallBack(fileName: String) extends Callback {

    /**
      * A callback method the user can implement to provide asynchronous handling of request completion.
      * This method will be called when the record sent to the server has been acknowledged.
      * Exactly one of the arguments will be non-null.
      *
      * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
      *                  occurred.
      * @param exception The exception thrown during processing of this record. Null if no error occurred.
      */
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
      if (metadata != null) println(s"Producer: $fileName - content consumed.") else exception.printStackTrace()
  }

  /**
    * Read file content.
    *
    * @param file the file
    * @throws IOException Signals that an I/O exception has occurred.
    */
  def pushFileContent(file: File): Unit = {
    println(s"Pushing file: $file")

    val aFile     = new RandomAccessFile(file, "r")
    val inChannel = aFile.getChannel
    val buffer    = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size())
    buffer.load()
    val strBuilder = new StringBuilder()
    (0 until buffer.limit).foreach { _ =>
      strBuilder.append(buffer.get.toChar)
    }
    buffer.clear() // do something with the data and clear/compact it.
    inChannel.close()
    aFile.close()

    val key   = rnd.nextInt(255)
    val value = strBuilder.toString()

    producer.send(new ProducerRecord[Integer, String](topic, key, value), new DemoCallBack(file.getName))

    file.delete()
  }

  /**
    * The Class WatchDir: once directory is clean, it watches for new files added.
    */
  private class WatchDir(directory: Path) extends Thread {

    /**
      * Creates a WatchService and registers the given directory
      */
    val watcher: WatchService = FileSystems.getDefault.newWatchService()
    val key: WatchKey         = directory.register(watcher, ENTRY_CREATE)

    override def start(): Unit = {
      val file = directory.toFile

      file
        .listFiles(new FileFilter() {
          override def accept(pathname: File): Boolean =
            pathname.isFile && !pathname.isHidden
        })
        .foreach { temp =>
          try pushFileContent(temp)
          catch {
            case ex: IOException => ex.printStackTrace()
          }
        }
      run()
    }

    /**
      * Process all events for keys queued to the watcher
      */
    override def run(): Unit =
      while (key.reset()) key.pollEvents().asScala.foreach {
        case event if event.kind == ENTRY_CREATE =>
          // Context for directory entry event is the file name of entry
          val name  = event.context().asInstanceOf[Path]
          val child = directory.resolve(name)

          try if (!Files.isDirectory(child, NOFOLLOW_LINKS)) pushFileContent(child.toFile)
          catch {
            case ex: IOException => ex.printStackTrace()
          }
      }
  }
}
