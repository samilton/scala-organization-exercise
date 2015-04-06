package com.pragmafs.dataplant

import java.lang.management.ManagementFactory
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory
import com.pragmafs.dataplant.ProcessContext

import scala.collection.mutable.ArrayBuffer


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

  val runtimeMxBean = ManagementFactory.getRuntimeMXBean();
  val arguments = runtimeMxBean.getInputArguments();
  StatusPrinter.print(LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext])

  logger.info("JVM Options are [ %s ]".format(arguments.toArray().mkString(", ")))
  logger.info("Options are [ %s ]".format(args.mkString(", ")))

  // TODO this group of methods is messy. there has to be a better way to compose this. (more java than scala)
  // TODO Need to get some error handling on here. Maybe this should be an object itself?
  def getObjectToRun(clazz: String): DataProcess = {
    Class.forName(clazz).newInstance().asInstanceOf[DataProcess]
  }

  // TODO Again there needs to be some error handling here. What if there are no options?
  def cleanArguments(args: Array[String]): Array[String] = {
    var argz = new ArrayBuffer[String]
    argz = argz ++ args
    if(argz.contains("--date")) {
      argz.remove(argz.indexOf("--date"), 2)
    }

    argz.slice(1, args.length).toArray

  }

  def isHoliday(args: Array[String]) = { args.contains("--holiday")}

  def getDate(args: Array[String]): Option[String] = {
    if(args.contains("--date")) {
      val date = args(args.indexOf("--date") + 1)
      // remove date from the options?
      Some(date)
    } else {
      None
    }
  }

  val dataProcessToRun = getObjectToRun(args(0))
  var processArguments = cleanArguments(args)
  var date = getDate(args)

  // maybe instead of an "ProcessContext" which is very "java-like" this should be a map?
  // recalling my original idea it was to allow the date argument to be used an what it is
  // an Option. The same for holidays so maybe using an object here makes sense.
  val context = new ProcessContext(args(0), processArguments, date)
  dataProcessToRun.execute(context)

}
