import sbt._
import Versions._

object Dependencies {

  implicit class Exclude(module: ModuleID) {
    def log4jExclude: ModuleID =
      module.excludeAll(ExclusionRule("log4j"))

    def driverExclusions: ModuleID =
      module.log4jExclude
        .exclude("com.google.guava", "guava")
        .excludeAll(ExclusionRule("org.slf4j"))
  }

  val kafka        = "org.apache.kafka"           %% "kafka"          % KafkaVersion
  val kafkaClient        = "org.apache.kafka"           % "kafka-clients"          % KafkaVersion
  val commonsCli        = "commons-cli"                % "commons-cli"          % CommonsCliVersion
}