package com.droidswarm.simulation

class Point(val x: Double, val y: Double) {

  def + (operand: Vector): Vector = new Vector (x + operand.x, y + operand.y)
  def -> (operand: Point): Vector = new Vector (operand.x - x, operand.y - y)


  override def toString = x + "," + y
}

class Vector(x: Double, y: Double) extends Point(x,y) {

  def / (operand: Double): Vector = new Vector (x / operand, y / operand)
  def * (operand: Double): Vector = new Vector (x * operand, y * operand)
  def * (operand: Int): Vector = new Vector (x * operand, y * operand)

  // Using java.lang.Math here not the scala.Math object as the scala version does not have
  // the handy dandy hypot method. It's not hard to calculate the hypot by hand but this way
  // is more terse
  def magnitude: Double = java.lang.Math.hypot(x,y)

  def normalise: Vector = {
    this / magnitude
  }

  def inverse = this * -1

  def rotate(angle: Double): Vector = {
    new Vector(x * Math.cos(angle) - y * Math.sin(angle),
               x * Math.sin(angle) + y * Math.cos(angle))
  }

}