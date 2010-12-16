package com.droidswarm.app

object Settings {

  var numberOfSwarmers = 50

  def trailLength = 25

  val minRotation = (10 / 180) * Math.Pi
  val maxRotation = (20 / 180) * Math.Pi

  def velocity = 5

  var worldSizeX = 100
  var worldSizeY = 100
}