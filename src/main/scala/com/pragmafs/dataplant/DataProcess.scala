package com.pragmafs.dataplant

/**
 * Created by shamilton on 3/27/15.
 */
trait DataProcess {

  def execute(args: Array[String])
  def usage()

}
