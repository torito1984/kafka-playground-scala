package com.kafka.message.example.launch

import com.kafka.message.example.core.{KafkaMailConsumer, KafkaMailProducer, KafkaMailProperties}
import com.kafka.message.example.util.{CommandLineHandler, DefaultProperties, PropertyKeys}
import org.apache.commons.cli.Option
import org.apache.commons.cli.ParseException
import com.kafka.message.example.util.GlobalNames._

object MailConsumerProducerDemo {

  /**
    * The main method.
    *
    * @param args the arguments
    */
  def main(args: Array[String]): Unit =
    try {
      val commandLine = new CommandLineHandler(getProducerOptions, args)

      val topic  = commandLine.getOption(TopicOptionMame)
      val path   = commandLine.getOption(PathOptionName)
      val offset = commandLine.getOption(OffsetOptionName)
      val group  = commandLine.getOption(GroupOptionName)

      println(s"Init Producer - Topic: $topic, path: $path")

      // start producer thread
      new KafkaMailProducer(topic.getOrElse(KafkaMailProperties.topic),
                            path.getOrElse(DefaultProperties.getPropertyValue(PropertyKeys.MailDirectory))).start()

      println(s"Init Consumer - Topic: $topic, offset: $offset, groupId: $group")

      //start consumer thread
      new KafkaMailConsumer(topic.getOrElse(KafkaMailProperties.topic),
                            offset.getOrElse("latest"),
                            group.getOrElse(KafkaMailProperties.groupId)).start()
    } catch {
      case e: ParseException => e.printStackTrace()
    }

  /**
    * Gets the producer options.
    *
    * @return the producer options
    */
  private def getProducerOptions =
    List(
      new Option(TopicOptionMame, TopicOptionMame, true, "topic name on which message is going to be published"),
      new Option(PathOptionName,
                 PathOptionName,
                 true,
                 "directory path from where message content going to be consumed."),
      new Option(OffsetOptionName, OffsetOptionName, true, "offset for the consumer to start from"),
      new Option(GroupOptionName, GroupOptionName, true, "offset for the consumer to start from")
    )
}
