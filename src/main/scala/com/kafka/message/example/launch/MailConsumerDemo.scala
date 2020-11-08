package com.kafka.message.example.launch

import com.kafka.message.example.core.{KafkaMailConsumer, KafkaMailProperties}
import com.kafka.message.example.util.CommandLineHandler
import com.kafka.message.example.util.GlobalNames._
import org.apache.commons.cli.{Option, ParseException}

object MailConsumerDemo {

  /**
    * Gets the consumer options.
    *
    * @return the consumer options
    */
  private def getConsumerOptions =
    List(
      new Option(TopicOptionMame, TopicOptionMame, true, "topic name on which message is going to be published"),
      new Option(OffsetOptionName, OffsetOptionName, true, "group the consumer appertains to"),
      new Option(GroupOptionName, GroupOptionName, true, "offset for the consumer to start from")
    )

  /**
    * The main method.
    *
    * @param args the arguments
    */
  def main(args: Array[String]): Unit =
    try {
      val commandLine = new CommandLineHandler(getConsumerOptions, args)

      val topic  = commandLine.getOption(TopicOptionMame)
      val offset = commandLine.getOption(OffsetOptionName)
      val group  = commandLine.getOption(GroupOptionName)

      println(s"Init Consumer - Topic: $topic, offset: $offset, groupId: $group")

      //start consumer thread
      new KafkaMailConsumer(topic.getOrElse(KafkaMailProperties.topic),
                            offset.getOrElse("latest"),
                            group.getOrElse(KafkaMailProperties.groupId)).start()
    } catch {
      case e: ParseException => e.printStackTrace()
    }

}
