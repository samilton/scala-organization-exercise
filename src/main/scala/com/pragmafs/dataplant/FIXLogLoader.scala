package com.pragmafs.dataplant

import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory
import java.util.Date

/**
 * Created by shamilton on 3/27/15.
 */
class FIXLogLoader extends DataProcess {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private class Conf(args: Seq[String]) extends ScallopConf(args) {
    val trading = opt[Boolean](required = false, noshort = true, descr = "Some sort of crap about crap")
  }

  // override FTW
  override def usage(): Unit = {
    println("foo")
  }

  // override FTW
  override def execute(args: List[String], date: Option[Date]) = {
    logger.info("I am running (non context based)")

  }

  // override FTW
  override def execute(context: ProcessContext): Unit = {
    val conf = new Conf(context.arguments)

    // I'd prefer to do the defaulting outside this class; otherwise every
    // DataProcess impl will have this exact logic
    val date = context.date.getOrElse(new Date())

    if (context.holiday) {
      logger.info("today is a holiday")
    }

    // string interpolation
    logger.info(s"running against [$date]")

    logger.info("I am running (context based)")
  }

}
