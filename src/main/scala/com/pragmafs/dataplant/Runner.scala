package com.pragmafs.dataplant

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory


/*
 * This is an overly simplistic example of how we could use very basic
 * reflection combined with a trait for any data process to be able to
 * handle deployment. The idea here is that we have this common entry
 * point with a main that takes as arguments a fully-qualified class name and
 * then the array of other arguments. The first argument must be the object
 * you want to run everything else will be based to that object.
 *
 * The object must use the DataProcess trait which defines an execute method.
 *
 * The execute method will receive an Array[String] and do what it wants with them.
 *
 * This combined with an executable jar (either through java -jar runner.jar) or
 * by pre-pending a shell script onto the jar. We can debate the merits of this.
 *
 * My suggestion would be that this jar could also contain a common logback.xml
 * and any other common resources (I'll get in trouble here but graphite config,
 * sentry config, etc).
 */
object Runner extends App {

  private[this] val logger = LoggerFactory.getLogger(Runner.getClass)

  StatusPrinter.print((LoggerFactory.getILoggerFactory).asInstanceOf[LoggerContext])

  def getObjectToRun(clazz: String): DataProcess = {
    Class.forName(clazz).newInstance().asInstanceOf[DataProcess]
  }

  def cleanArguments(args: Array[String]): Array[String] = {
    args.slice(1, args.length)
  }

  var dataProcessToRun = getObjectToRun(args(0))
  val actualArguments = cleanArguments(args)

  logger.info("Executing [%s] with [%s]".format(dataProcessToRun.getClass, actualArguments.mkString(", ")))

  dataProcessToRun.execute(actualArguments)

}
