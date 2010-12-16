package com.droidswarm.simulation

class Point(val x: Float, val y: Float) {

  def + (operand: Vector): Vector = new Vector (x + operand.x, y + operand.y)
  def -> (operand: Point): Vector = new Vector (operand.x - x, operand.y - y)


  override def toString = x + "," + y
}

class Vector(x: Float, y: Float) extends Point(x,y) {

  def / (operand: Float): Vector = new Vector (x / operand, y / operand)
  def * (operand: Float): Vector = new Vector (x * operand, y * operand)
  def * (operand: Int): Vector = new Vector (x * operand, y * operand)

  // Using java.lang.Math here not the scala.Math object as the scala version does not have
  // the handy dandy hypot method. It's not hard to calculate the hypot by hand but this way
  // is more terse
  def magnitude: Float = (java.lang.Math.hypot(x,y)).toFloat

  def normalise: Vector = {
    this / magnitude
  }

  def inverse = this * -1

  def rotate(angle: Float): Vector = {
    new Vector((x * Math.cos(angle) - y * Math.sin(angle)).toFloat,
               (x * Math.sin(angle) + y * Math.cos(angle)).toFloat)
  }

}