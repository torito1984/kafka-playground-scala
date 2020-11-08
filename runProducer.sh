#/usr/bin/bash

java -cp target/scala-2.12/kafka-message-scala-example-assembly-1.0.0.jar com.kafka.message.example.launch.MailProducerDemo --path ${1} --topic ${2}