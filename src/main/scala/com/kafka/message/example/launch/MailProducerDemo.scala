package com.kafka.message.example.launch

import com.kafka.message.example.core.{KafkaMailProducer, KafkaMailProperties}
import com.kafka.message.example.util.GlobalNames._
import com.kafka.message.example.util.{CommandLineHandler, DefaultProperties, PropertyKeys}
import org.apache.commons.cli.{Option, ParseException}

/**
  * The Class MailProducerDemo
  */
object MailProducerDemo {

  /**
    * The main method.
    *
    * @param args the arguments
    */
  def main(args: Array[String]): Unit =
    try {
      val commandLine = new CommandLineHandler(getProducerOptions, args)

      val topic = commandLine.getOption(TopicOptionMame)
      val path  = commandLine.getOption(PathOptionName)

      println(s"Init Producer - Topic: $topic, path: $path")

      // start producer thread
      val producerThread = new KafkaMailProducer(
        topic.getOrElse(KafkaMailProperties.topic),
        path.getOrElse(DefaultProperties.getPropertyValue(PropertyKeys.MailDirectory))
      )
      producerThread.start()

    } catch {
      case e: ParseException => e.printStackTrace()
    }

  /**
    * Gets the producer options.
    *
    * @return the producer options
    */
  protected def getProducerOptions: List[Option] =
    List(
      new Option(TopicOptionMame, TopicOptionMame, true, "topic name on which message is going to be published"),
      new Option(PathOptionName,
                 PathOptionName,
                 true,
                 "directory path from where message content going to be consumed.")
    )
}
