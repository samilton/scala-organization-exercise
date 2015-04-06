package com.pragmafs.dataplant

import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory

/**
 * Created by shamilton on 3/27/15.
 */
class FIXLogLoader extends DataProcess {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private class Conf(args: Seq[String]) extends ScallopConf(args) {
    val trading = opt[Boolean](required = false, noshort = true, descr = "Some sort of crap about crap")
  }

  def usage(): Unit = {
    println("foo")
  }

  def execute(args: Array[String], date: Option[String] = None) = {
    logger.info("I am running (non context based)")

  }

  def execute(context: ProcessContext): Unit = {
    val conf = new Conf(context.arguments)
    val date = context.date.getOrElse("20150501")

    if(context.holiday.get) {
      logger.info("today is a holiday")
    }

    logger.info("running against [%s]".format(date))
    logger.info("I am running (context based)")
  }

}
