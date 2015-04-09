package com.pragmafs.dataplant

import java.util.Date

/**
 * Created by shamilton on 3/27/15.
 */
trait DataProcess {

  def execute(context: ProcessContext)
  def usage()

}
