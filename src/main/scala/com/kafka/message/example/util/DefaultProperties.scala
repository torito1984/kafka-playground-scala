package com.kafka.message.example.util

import java.io.IOException
import java.util.Properties

object DefaultProperties {

  val prop = new Properties()

  try prop.load(getClass.getResourceAsStream("/kafka-message-server-example-properties.prop"))
  catch {
    case ex: IOException => ex.printStackTrace()
  }

  /**
    * Gets the property value.
    *
    * @param propertyKey the property key
    * @return the property value
    */
  def getPropertyValue(propertyKey: String): String =
    prop.getProperty(propertyKey)
}
