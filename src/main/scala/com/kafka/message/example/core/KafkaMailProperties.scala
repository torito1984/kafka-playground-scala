package com.kafka.message.example.core

object KafkaMailProperties {
  val topic: String          = "myTopic"
  val groupId: String        = "myGroupId"
  val kafkaServerURL: String = "localhost"
  val kafkaServerPort: Int   = 19093

  val kafkaProducerBufferSize: Int = 64 * 1024
  val connectionTimeOut: Int       = 100000
  val reconnectInterval: Int       = 10000
}
