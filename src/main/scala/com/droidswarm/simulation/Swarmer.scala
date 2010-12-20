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
//      val alignVector = new Vector(0,0)

      desires = cohesionVector + avoidVector + alignVector + desires
      s.desires = cohesionVector.inverse + avoidVector.inverse + alignVector + s.desires

      swarmersProcessed = s :: swarmersProcessed
      s.swarmersProcessed = this :: s.swarmersProcessed

      if (debug && s.debug) {

        Log.d("swarmer "+id, "cohesion vector =" + cohesionVector.magnitude)
        Log.d("swarmer "+id, "avoid vector =" + avoidVector.magnitude)
        Log.d("swarmer "+id, "align vector =" + alignVector.magnitude)
        Log.d("swarmer "+id, "desire vector =" + desires.magnitude)
      }
    }
  }

  def avoidTouch(touch: Point) {
    desires = avoidTouch(currentPosition, touch) + desires
//    if (debug) {
//      Log.d("swarmer "+id, "touch component =" + avoidTouch(currentPosition, touch).magnitude)
//    }
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

//    val desiredRotation =
//      (Math.atan2(desiredHeading.y, desiredHeading.x) - Math.atan2(currentHeading.y, currentHeading.x)).toFloat


    val desiredRotation = currentHeading.angleWith(desiredHeading)

    Math.abs(desiredRotation) match {
      case d: Float if d < maxRotation => currentHeading = desiredHeading
      case _ => {
        val positiveRotateVector = currentHeading.rotate(maxRotation)
        val negativeRotateVector = currentHeading.rotate(-maxRotation)

        val positiveAngle = desiredHeading.angleWith(positiveRotateVector)
        val negativeAngle = desiredHeading.angleWith(negativeRotateVector)

        if (positiveAngle < negativeAngle) {
          currentHeading = positiveRotateVector
        } else {
          currentHeading = negativeRotateVector
        }
      }
    }

  }


  def initialise {
    velocity = Random.nextFloatInRange(Settings.minVelocity, Settings.maxVelocity)

    maxRotation = Random.nextFloatInRange(Settings.minRotation, Settings.maxRotation)

    currentHeading = new Vector(
      Random.nextFloatInRange(-1, 1),
      Random.nextFloatInRange(-1, 1)
    ).normalise


    currentPosition = new Vector(
      Random.nextFloatInRange(0, Settings.worldSizeX),
      Random.nextFloatInRange(0, Settings.worldSizeY)
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

class Preditor(id: Int) extends Swarmer(id) {

  override def initialise {
    super.initialise
    velocity = Random.nextFloatInRange(Settings.minVelocity + 2, Settings.maxVelocity + 2)
  }

  override def calculateInteractions(swarmers: List[Swarmer]) {
    swarmers.diff(this :: swarmersProcessed) foreach { s =>

      val chasePackVector = chasePack(currentPosition, s.currentPosition)
      val chaseIndividualVector = chaseIndividual(currentPosition, s.currentPosition)

      val swarmerFleeVector = fleePreditor(s.currentPosition, currentPosition)

      desires = chasePackVector + chaseIndividualVector + desires
      s.desires = swarmerFleeVector + s.desires

      swarmersProcessed = s :: swarmersProcessed
      s.swarmersProcessed = this :: s.swarmersProcessed

//      if (debug && s.debug) {
//
//        Log.d("swarmer "+id, "cohesion vector =" + cohesionVector.magnitude)
//        Log.d("swarmer "+id, "avoid vector =" + avoidVector.magnitude)
//        Log.d("swarmer "+id, "align vector =" + alignVector.magnitude)
//        Log.d("swarmer "+id, "desire vector =" + desires.magnitude)
//      }
    }
  }

}