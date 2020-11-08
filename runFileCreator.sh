#/usr/bin/bash

mkdir -p ${1}
java -cp target/scala-2.12/kafka-message-scala-example-assembly-1.0.0.jar  com.kafka.message.example.other.launch.CreateFile --path ${1} --sleepTime ${2}