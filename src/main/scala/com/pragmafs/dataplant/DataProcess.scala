package com.pragmafs.dataplant

import java.util.Date

/**
 * Created by shamilton on 3/27/15.
 */
trait DataProcess {

  def execute(context: ProcessContext)

  // Strings are poor dates
  // List is better than Array
  // Are you sure you want this? What's the use case for a no-context execution?
  def execute(args: List[String], date: Option[Date] = None)

  def usage()

}
