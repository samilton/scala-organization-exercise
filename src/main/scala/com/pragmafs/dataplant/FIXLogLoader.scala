package com.pragmafs.dataplant

import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory
import java.util.Date

class FIXLogLoader extends DataProcess {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private class Conf(args: Seq[String]) extends ScallopConf(args) {
    val database = opt[String](required = false, noshort = true, descr = "JDBC URL")
    val domain = opt[String](required = false, noshort = true, descr = "Client Environment to against")
  }

  // override FTW
  override def usage(): Unit = {
    println("foo")
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
    logger.info(s"running against [$date] connecting to ${conf.database()} for client ${conf.domain()}")

  }

}
