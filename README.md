Kafka Example Application
========================================

Kafka example application

This project shows the basics of Kafka queue system. The code simulates a producer that is 
reading a set of emails to send to Kafka and a consumer that picks them to be processed. The pieces of the code are:

- KafkaMailConsumer: Consumer that picks mails from the Kafka queue.
- KafkaMailProducer: Producer that picks mails from the filesystem and queues them in kafka.
- CreateFile: Simulates the creation of files in a folder.

In addition, and for practice purposes, CustomPartitioner shows how to write a custom partitioner for sharding a topic.

This code supposes that a Kafka installation is available. There is a docker compose in the code to run a local Kafka with 3 nodes.
```
$ cd docker
$ docker-compose up -d
```
Instructions to run
========================================

In order to run the code, 

- **runFileCreator.sh** triggers the mail creation simulation. Run one of this.
```
$ ./runFileCreator.sh /tmp/kafka-test 10
```
- **runProducer.sh** runs the producer to Kafka. It takes as arguments the folder where the FileCreator is pushing the emails 
and the topic where to publish the messages. 
Kafka installation is configured to listen in localhost:19093. This default can be changed with a simple change in the code.
```
$ ./runProducer.sh /tmp/kafka-test myTopic
```
- **runConsumer.sh** runs the mail consumer. You can run as many consumers as you want (note that if you run more consumers 
than partitions of the topic some of them will remain idle). It takes as arguments the topic, 
the point where to start consuming (e.g. earliest) and the name of the consumer group.
```
$ ./runConsumer.sh myTopic earliest myGroupId 
```

