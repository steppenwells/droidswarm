package com.droidswarm.simulation

import com.droidswarm.app.Settings
import com.droidswarm.util.Random
import android.util.Log

class Swarmer(val id: Int) extends Equals with Intentions {


  var currentHeading: Vector = new Vector(0,0)
  var currentPosition: Point = new Vector(0,0)

  var previousPositions: List[Point] = Nil

  var velocity: Float = 5
  var maxRotation: Float = 0.26f

  var desires: Vector = new Vector(0,0)
  var swarmersProcessed: List[Swarmer] = Nil

  var debug = false

  def clearDesires {
    desires = new Vector(0,0)
    swarmersProcessed = Nil
  }

  def calculateInteractions(swarmers: List[Swarmer]) {
    swarmers.diff(this :: swarmersProcessed) foreach { s =>

      val cohesionVector = cohesion(currentPosition, s.currentPosition)
      val avoidVector = avoid(currentPosition, s.currentPosition)
      val alignVector = align(currentHeading, currentPosition, s.currentHeading, s.currentPosition)

      desires = cohesionVector + avoidVector + alignVector + desires
      s.desires = cohesionVector.inverse + avoidVector.inverse + alignVector + s.desires

      swarmersProcessed = s :: swarmersProcessed
      s.swarmersProcessed = this :: s.swarmersProcessed

//      if (debug) {
//
//        Log.d("swarmer", "cohesion vector =" + cohesionVector)
//        Log.d("swarmer", "avoid vector =" + avoidVector)
//        Log.d("swarmer", "align vector =" + alignVector)
//        Log.d("swarmer", "desire vector =" + desires)
//      }
    }
  }

  def updatePosition {

//    if (debug) {
//
//      Log.d("swarmer" + id, "old position  =" + currentPosition)
//    }

    rotateTowards (desires normalise)

    previousPositions = (currentPosition :: previousPositions) take Settings.trailLength

    val movement = currentHeading * velocity
    val nextPosition = new Point(currentPosition.x + movement.x, currentPosition.y + movement.y)

    currentPosition = wrapWorld(nextPosition)

//    if (debug) {
//
//      Log.d("swarmer" + id, "normal desire vector =" + desires.normalise)
//      Log.d("swarmer" + id, "movement vector =" + movement)
//      Log.d("swarmer" + id, "next Position vector =" + nextPosition)
//      Log.d("swarmer" + id, "now at vector =" + currentPosition)
//    }

  }

  private def wrapWorld(position: Point) = {
    val x = position.x match {
      case d: Float if d < 0 => d + Settings.worldSizeX
      case d: Float if d > Settings.worldSizeX => d - Settings.worldSizeX
      case d: Float => d
    }

    val y = position.y match {
      case d: Float if d < 0 => d + Settings.worldSizeY
      case d: Float if d > Settings.worldSizeY => d - Settings.worldSizeY
      case d: Float => d
    }

    new Point(x,y)
  }

  def rotateTowards(desiredHeading: Vector) = {
//
//    if(debug) {
//      Log.d("swarmer" + id, "old heading =" + currentHeading)
//      Log.d("swarmer" + id, "rotating towards heading =" + desiredHeading)
//      Log.d("swarmer" + id, "maxRotation =" + maxRotation)
//    }
    val desiredRotation =
      (Math.atan2(desiredHeading.y, desiredHeading.x) - Math.atan2(currentHeading.y, currentHeading.x)).toFloat
//    Log.d("swarmer", "desired heading = "+desiredHeading)
//    Log.d("swarmer", "desired rotation = "+desiredRotation)
    Math.abs(desiredRotation) match {
      case d: Float if d < maxRotation => currentHeading = desiredHeading
      case _ => desiredRotation match {
        case d: Float if d > 0 => currentHeading = currentHeading.rotate(maxRotation)
        case d: Float if d < 0 => currentHeading = currentHeading.rotate(-maxRotation)
        case _ => 
      }
    }
//    if(debug) {
//
//      Log.d("swarmer" + id, "desired rotation =" + desiredRotation)
//      Log.d("swarmer" + id, "new heading =" + currentHeading)
//    }
  }


  def initialise {
    velocity = Settings.velocity

    maxRotation = Random.nextFloatInRange(Settings.minRotation, Settings.maxRotation)

    currentHeading = new Vector(
      Random.nextFloatInRange(-1, 1),
      Random.nextFloatInRange(-1, 1)
    ).normalise


    currentPosition = new Vector(
      Random.nextFloatInRange(0, Settings.worldSizeX),
      Random.nextFloatInRange(0, Settings.worldSizeY)
    )

    if (debug) {
      Log.d("swarmer", "init, position "+currentPosition)
    }
  }


  override def equals(other: Any): Boolean =
    other match {
      case s: Swarmer => id == s.id
      case _=> false
    }

  override def canEqual(other: Any) = other.isInstanceOf[Swarmer]

  override def hashCode = id
}