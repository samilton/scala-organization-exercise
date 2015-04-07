package com.pragmafs.dataplant

import java.util.Date

/**
 * Created by shamilton on 3/31/15.
 */
case class ProcessContext(
  var clazz: String,
  var arguments: List[String],  // Array is mutable and therefore poo
  var date: Option[Date],       // If a process *needs* a Date, this shouldn't be an Option
  var holiday: Boolean          // Option[Boolean] is a bit wierd here I think
)
