#/usr/bin/bash

java -cp target/scala-2.12/kafka-message-scala-example-assembly-1.0.0.jar com.kafka.message.example.launch.MailConsumerDemo --topic ${1} --offset ${2} --group ${3}
