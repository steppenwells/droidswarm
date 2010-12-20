package com.droidswarm.simulation

import com.droidswarm.app.Settings

trait Intentions {

  def cohesion(selfPosition: Point, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val direction: Vector = separation.normalise
    val linearProximityFactor = (100 / separation.magnitude).toFloat
    val root2ProximityFactor = (20000 / Math.pow(separation.magnitude, 2)).toFloat

    (direction * root2ProximityFactor) + (direction * linearProximityFactor) //+ (direction * 100)
  }

  def avoidTouch(selfPosition: Point, touchPosition: Point): Vector = {
    val separation: Vector = selfPosition -> touchPosition
    val direction: Vector = separation.normalise
    val linearProximityFactor = (-20000 / separation.magnitude).toFloat

    direction * linearProximityFactor * Settings.touchDirection
  }

  def avoid (selfPosition: Point, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val direction: Vector = separation.normalise
    val proximityFactor = (-1000000 / Math.pow(separation.magnitude, 4)).toFloat

    direction * 10 * proximityFactor
  }

  def align (selfDirection: Vector, selfPosition: Point, otherDirection: Vector, otherPosition: Point): Vector = {
    val separation: Vector = selfPosition -> otherPosition
    val averageHeading = (selfDirection + otherDirection) normalise
    val proximityFactor = (10000 / Math.pow(separation.magnitude, 2)).toFloat

    averageHeading * proximityFactor
  }

  def chasePack(predPosition: Point, preyPosition: Point): Vector = {
    val separation: Vector = predPosition -> preyPosition
    val direction: Vector = separation.normalise
    val linearProximityFactor = (100 / separation.magnitude).toFloat
    val root2ProximityFactor = (20000 / Math.pow(separation.magnitude, 2)).toFloat

    (direction * root2ProximityFactor) + (direction * linearProximityFactor)
  }

  def chaseIndividual(predPosition: Point, preyPosition: Point): Vector = {
    val separation: Vector = predPosition -> preyPosition
    val direction: Vector = separation.normalise
    val proximityFactor = (1000000 / Math.pow(separation.magnitude, 4)).toFloat

    direction * 10 * proximityFactor
  }

  def fleePreditor(preyPosition: Point, predPosition: Point): Vector = {
    val separation: Vector = preyPosition -> predPosition
    if (separation.magnitude < 150) {
      val direction: Vector = separation.normalise
      val linearProximityFactor = (-5000 / separation.magnitude).toFloat

      direction * linearProximityFactor
    } else {
      new Vector(0,0)
    }

  }
}