package com.droidswarm.simulation

trait Intentions {

  def cohesion(selfPosition: Point, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val direction: Vector = separation.normalise
    val linearProximityFactor = (100000 / separation.magnitude).toFloat
    val root2ProximityFactor = (200000 / Math.pow(separation.magnitude, 2)).toFloat

    (direction * root2ProximityFactor) + (direction * linearProximityFactor) + (direction * 1000)
  }

  def avoidTouch(selfPosition: Point, touchPosition: Point): Vector = {
    val separation: Vector = selfPosition -> touchPosition
    val direction: Vector = separation.normalise
    val linearProximityFactor = (-10000000 / separation.magnitude).toFloat

    direction * linearProximityFactor
  }

  def avoid (selfPosition: Point, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val direction: Vector = separation.normalise
    val proximityFactor = (-500000000 / Math.pow(separation.magnitude, 4)).toFloat

    direction * proximityFactor
  }

  def align (selfDirection: Vector, selfPosition: Point, otherDirection: Vector, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val averageHeading = (selfDirection + otherDirection) normalise
    val proximityFactor = (150 / Math.pow(separation.magnitude, 3)).toFloat

    averageHeading * proximityFactor
  }
}