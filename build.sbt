import Dependencies._

name := "kafka-message-scala-example"
organization := "com.kafka"
version := "1.0.0"
scalaVersion := Versions.Scala_2_12_Version
crossScalaVersions := Versions.CrossScalaVersions
scalacOptions := Seq("-unchecked", "-deprecation", "-Ywarn-unused-import")


parallelExecution in Test := false


libraryDependencies ++= Seq(
  kafka excludeAll (ExclusionRule("org.slf4j", "slf4j-log4j12"), ExclusionRule("org.apache.zookeeper",
                                                                                      "zookeeper")),
  kafkaClient excludeAll (ExclusionRule("org.slf4j", "slf4j-log4j12"), ExclusionRule("org.apache.zookeeper",
    "zookeeper")),
  commonsCli
)

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true)
test in assembly := {}
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}