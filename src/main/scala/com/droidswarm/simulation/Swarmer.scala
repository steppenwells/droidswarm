package com.droidswarm.simulation

import com.droidswarm.app.Settings
import com.droidswarm.util.Random

class Swarmer(val id: Int) extends Equals with Intentions {


  var currentHeading: Vector = new Vector(0,0)
  var currentPosition: Point = new Vector(0,0)

  var previousPositions: List[Point] = Nil

  var velocity: Double = 5
  var maxRotation: Double = 15/180 * Math.Pi

  var desires: Vector = new Vector(0,0)
  var swarmersProcessed: List[Swarmer] = Nil


  def clearDesires {
    desires = new Vector(0,0)
    swarmersProcessed = Nil
  }

  def calculateInteractions(swarmers: List[Swarmer]) {
    swarmers.diff(swarmersProcessed) foreach { s =>

      val cohesionVector = cohesion(currentPosition, s.currentPosition)
      val avoidVector = avoid(currentPosition, s.currentPosition)
      val alignVector = align(currentHeading, currentPosition, s.currentHeading, s.currentPosition)

      desires = cohesionVector + avoidVector + alignVector + desires
      s.desires = cohesionVector.inverse + avoidVector.inverse + alignVector + s.desires

      swarmersProcessed = s :: swarmersProcessed
      s.swarmersProcessed = this :: s.swarmersProcessed
    }
  }

  def updatePosition {

    rotateTowards (desires normalise)

    previousPositions = (currentPosition :: previousPositions) take Settings.trailLength

    val movement = currentHeading * velocity
    val nextPosition = new Point(currentPosition.x + movement.x, currentHeading.y + movement.y)

    currentPosition = wrapWorld(nextPosition)

  }

  private def wrapWorld(position: Point) = {
    val x = position.x match {
      case d: Double if d < 0 => d + Settings.worldSizeX
      case d: Double if d > Settings.worldSizeX => d - Settings.worldSizeX
      case d: Double => d
    }

    val y = position.y match {
      case d: Double if d < 0 => d + Settings.worldSizeY
      case d: Double if d > Settings.worldSizeY => d - Settings.worldSizeY
      case d: Double => d
    }

    new Point(x,y)
  }

  def rotateTowards(desiredHeading: Vector) = {
    val desiredRotation = Math.atan2(desiredHeading.y, desiredHeading.x) - Math.atan2(currentHeading.y, currentHeading.x)
    Math.abs(desiredRotation) match {
      case d: Double if d < maxRotation => currentHeading = desiredHeading
      case _ => desiredRotation match {
        case d: Double if d > 0 => currentHeading = currentHeading.rotate(maxRotation)
        case d: Double if d < 0 => currentHeading = currentHeading.rotate(-maxRotation)
      }
    }
  }


  def initialise {
    velocity = Settings.velocity

    maxRotation = Random.nextDoubleInRange(Settings.minRotation, Settings.maxRotation)

    currentHeading = new Vector(
      Random.nextDoubleInRange(-1, 1),
      Random.nextDoubleInRange(-1, 1)
    ).normalise


    currentPosition = new Vector(
      Random.nextDoubleInRange(0, Settings.worldSizeX),
      Random.nextDoubleInRange(0, Settings.worldSizeY)
    )
  }


  override def equals(other: Any): Boolean =
    other match {
      case s: Swarmer => id == s.id
      case _=> false
    }

  override def canEqual(other: Any) = other.isInstanceOf[Swarmer]

  override def hashCode = id
}