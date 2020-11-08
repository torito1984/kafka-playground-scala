package com.kafka.message.example.core

import java.util.Properties

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._

class KafkaMailConsumer(topic: String, offset: String, groupId: String, port: Int = KafkaMailProperties.kafkaServerPort)
    extends Thread {
  private var running = true

  /**
    * Creates the consumer config.
    *
    * @return the consumer config
    */
  private def createConsumerConfig(offset: String, groupId: String, port: Int): Properties = {
    val props = new Properties()
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KafkaMailProperties.kafkaServerURL + ":" + port)
    props.put("group.id", groupId)
    props.put("auto.commit.interval.ms", "1000")
    props.put("auto.offset.reset", offset)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  val consumer = new KafkaConsumer[Int, String](createConsumerConfig(offset, groupId, port))
  consumer.subscribe(List(topic).asJava)

  def setRunning(running: Boolean) {
    this.running = running
  }

  def notifyMessage(key: Int, value: String): Unit =
    println("Consumer: Received mail (" + key + ") " + value)

  override def run(): Unit =
    while (running) {
      val records = consumer.poll(java.time.Duration.ofMillis(1000))
      records.asScala.foreach { record =>
        notifyMessage(record.key(), record.value())
      }
    }
}
