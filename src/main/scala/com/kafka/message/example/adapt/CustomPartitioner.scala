package com.kafka.message.example.adapt

import java.util
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster

class CustomPartitioner extends Partitioner {

  override def partition(topic: String,
                         key: Any,
                         keyBytes: Array[Byte],
                         value: Any,
                         valueBytes: Array[Byte],
                         cluster: Cluster): Int = {
    val ikey = key.asInstanceOf[Int]
    ikey % cluster.partitionCountForTopic(topic)
  }

  override def close(): Unit = {
    // nothing to do
  }

  override def configure(configs: util.Map[String, _]): Unit = {
    // nothing to do
  }
}
