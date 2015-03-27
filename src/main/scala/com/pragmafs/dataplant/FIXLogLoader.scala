package com.pragmafs.dataplant

import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory

/**
 * Created by shamilton on 3/27/15.
 */
class FIXLogLoader extends DataProcess {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private class Conf(args: Seq[String]) extends ScallopConf(args) {
    val date = opt[String](required = true, noshort = true, descr = "The date for which to download in YYYYMMDD format")
  }

  def usage(): Unit = {
    println("foo")
  }

  def execute(args: Array[String]): Unit = {
    val conf = new Conf(args)

    logger.info("I am running")
  }

}
