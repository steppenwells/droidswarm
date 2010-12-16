package com.droidswarm.util

import util.Random


object Random {
  val r = new Random

  def nextDoubleInRange(min: Double, max: Double) = {
    val range = max - min
    (r.nextDouble * range) + min
  }
}