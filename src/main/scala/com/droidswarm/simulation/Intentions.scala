package com.droidswarm.simulation

trait Intentions {

  def cohesion(selfPosition: Point, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val direction: Vector = separation.normalise
    val proximityFactor = 10 / Math.pow(separation.magnitude, 2)

    direction * proximityFactor
  }

  def avoid (selfPosition: Point, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val direction: Vector = separation.normalise
    val proximityFactor = -5 / Math.pow(separation.magnitude, 4)

    direction * proximityFactor
  }

  def align (selfDirection: Vector, selfPosition: Point, otherDirection: Vector, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val averageHeading = (selfDirection + otherDirection) normalise
    val proximityFactor = 10 / Math.pow(separation.magnitude, 2)

    averageHeading * proximityFactor
  }
}