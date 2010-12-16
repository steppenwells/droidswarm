package com.droidswarm.util

import util.Random


object Random {
  val r = new Random

  def nextFloatInRange(min: Float, max: Float) = {
    val range = max - min
    (r.nextFloat * range) + min
  }
}