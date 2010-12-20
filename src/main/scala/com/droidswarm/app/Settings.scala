package com.droidswarm.app

object Settings {

  var numberOfSwarmers = 30
  var numberOfPreditors = 2

  def trailLength = 35

  val minRotation: Float = 0.175f
  val maxRotation: Float = 0.350f

  val minVelocity: Float = 4.75f
  val maxVelocity: Float = 5.25f

  def velocity = 5

  var worldSizeX = 100
  var worldSizeY = 100

  var touchDirection = 1
}