package com.droidswarm.simulation

class Swarmer(val id: Int) extends Equals {


  var currentHeading: Vector
  var currentPosition: Point

  var previousPositions: List[Point] = Nil

  var velocity: Double
  var maxRotation: Double

  var desires: List[Vector] = Nil

  def clearDesires {
    desires = Nil
  }

  def calculateInteractions(swarmers: List[Swarmer]) {}

  def updatePosition {}

  override def equals(other: Any): Boolean =
    other mtch {
      case s: Swarmer => id == s.id
      case _=> false
    }

  override def canEqual(other: Any) = other.isInstanceOf[Swarmer]

  override def hashCode = id
}