package com.pragmafs.dataplant

/**
 * Created by shamilton on 3/31/15.
 */
case class ProcessContext(var clazz: String,
                          var arguments: Array[String],
                          var date: Option[String],
                          var holiday: Option[Boolean] = Some(false))
