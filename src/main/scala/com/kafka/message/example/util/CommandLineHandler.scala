package com.kafka.message.example.util

import org.apache.commons.cli.{CommandLine, DefaultParser, Option, Options}

import scala.util.Try

/**
  * Instantiates a new kafka example command line handler.
  *
  * @param optionList the option list
  * @param args       the args
  */
class CommandLineHandler(val optionList: List[Option], args: Array[String]) {

  private val parser  = new DefaultParser()
  private val options = new Options()
  optionList.foreach(option => options.addOption(option))
  val commandLine: scala.Option[CommandLine] = Try(parser.parse(options, args)).toOption

  /**
    * Gets the option.
    *
    * @param option the option
    * @return the option
    */
  def getOption(option: String): scala.Option[String] =
    commandLine.flatMap(cl => scala.Option(cl.getOptionValue(option)))

}
