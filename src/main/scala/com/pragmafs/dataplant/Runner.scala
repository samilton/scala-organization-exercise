package com.pragmafs.dataplant

import java.lang.management.ManagementFactory
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}
import java.util.Date
import java.text.SimpleDateFormat
import scala.annotation.tailrec


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
  // Adds implicits that let us convert from Java to Scala collections easily
  import scala.collection.JavaConversions._

  private[this] val logger = LoggerFactory.getLogger(Runner.getClass)

  // style - no need for () or ;
  val runtimeMxBean = ManagementFactory.getRuntimeMXBean

  // ugh; why the cast?
  StatusPrinter.print(LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext])

  // we don't use the val anywhere else, so let's just inline it (it's not that long)
  // now arguments is a List we can just use mkString directly
  // use string interpolation
  logger.info(s"JVM Options are [${runtimeMxBean.getInputArguments.toList mkString ", "}]")

  // less punctuation, use string interpolation
  logger.info(s"Options are [${args mkString ","}]")

  // TODO this group of methods is messy. there has to be a better way to compose this. (more java than scala)
  // TODO Need to get some error handling on here. Maybe this should be an object itself?
  // You could wrap this in a Try like this
  def getObjectToRun(clazz: String): Try[DataProcess] = Try {
    Class.forName(clazz).newInstance().asInstanceOf[DataProcess]
  }

  // Here's an idea for building a config object from the args... but maybe there's a
  // library out there to do this.
  case class Arguments(
    className: Option[String] = None,
    isHoliday: Boolean        = false,       // optional parameter, so this is a default value
    date:      Option[Date]   = None,
    otherArgs: List[String]   = Nil
  )

  @tailrec
  def parseArguments(args: Seq[String], a: Arguments = Arguments()): Arguments = args match {
    case Nil =>
      // no more args
      a

    case "--date" :: dateStr :: as =>
      // parse the date - maybe hoist this format object
      val d = new SimpleDateFormat("yyyyMMdd").parse(dateStr)
      parseArguments(as, a.copy(date = Some(d)))

    case "--holiday" :: as =>
      // holiday flag
      parseArguments(as, a.copy(isHoliday = true))

    case "--" :: as =>
      // gnu-style end of params marker; stop here
      a.copy(otherArgs = as)

    case string :: as =>
      // first string is the classname
      if (a.className.isDefined)
        sys.error("duplicate classname specified")
      else
        parseArguments(as, a.copy(className = Some(string)))
  }

  val arguments = parseArguments(args)

  val dataProcessToRun = arguments.className match {
    case Some(c) => getObjectToRun(c) match {
      case Success(d) => d
      case Failure(e) => sys.error(s"unable to load class $c; ${e.getMessage}")
    }
    case None    => sys.error("no classname specified")
  }

  // maybe instead of an "ProcessContext" which is very "java-like" this should be a map?
  // recalling my original idea it was to allow the date argument to be used an what it is
  // an Option. The same for holidays so maybe using an object here makes sense.
  val context = new ProcessContext(arguments.className.get, arguments.otherArgs, arguments.date, arguments.isHoliday)
  dataProcessToRun.execute(context)
}
