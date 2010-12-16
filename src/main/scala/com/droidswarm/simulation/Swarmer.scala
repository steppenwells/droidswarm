package com.droidswarm.simulation

import com.droidswarm.app.Settings
import com.droidswarm.util.Random

class Swarmer(val id: Int) extends Equals with Intentions {


  var currentHeading: Vector
  var currentPosition: Point

  var previousPositions: List[Point] = Nil

  var velocity: Double
  var maxRotation: Double

  var desires: Vector = New Vector(0,0)
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
    currentPosition = new Point(currentPosition.x + movement.x, currentHeading.y + movement.y)

  }

  def rotateTowards(desiredHeading: Vector) = {
    val desiredRotation = Math.atan2(desiredHeading.y, desiredHeading.x) - Math.atan2(currentHeading.y, currentHeading.x)
    Math.abs(desiredRotation) match {
      case d: Double if d < maxRotation => heading = desiredHeading
      case _ => desiredRotation match {
        case d: Double if d > 0 => currentHeading = heading.rotate(maxRotation)
        case d: Double if d < 0 => currentHeading = heading.rotate(-maxRotation)
      }
    }
  }


  def initialise {
    velocity = Settings.velocity

    maxRotation = Random.nextDoubleInRange(Setings.minRotation, Settings.maxRotation)

    currentHeading = new Vector
      (Random.nextDoubleInRange(-1, 1),
       Random.nextDoubleInRange(-1, 1)).normalise


    currentPosition = new Vector
      (Random.nextDoubleInRange(0, Settings.worldSizeX),
       Random.nextDoubleInRange(0, Settings.worldSizeY))
  }


  override def equals(other: Any): Boolean =
    other mtch {
      case s: Swarmer => id == s.id
      case _=> false
    }

  override def canEqual(other: Any) = other.isInstanceOf[Swarmer]

  override def hashCode = id
}