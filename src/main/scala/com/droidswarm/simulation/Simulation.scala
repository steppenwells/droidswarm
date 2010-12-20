package com.droidswarm.simulation

import com.droidswarm.app.Settings
import android.util.Log
import android.graphics.{Path, ColorFilter, Paint, Canvas}
import android.view.View.OnTouchListener
import android.view.{View, MotionEvent, SurfaceHolder}

class Simulation extends OnTouchListener {
  var swarmers: List[Swarmer] = Nil
  var preditors: List[Preditor] = Nil

  var touchPoints: List[Point] = Nil
  val touchLock: String = ""

  def initialise {

    swarmers = Nil
    for (i <- 1 to Settings.numberOfSwarmers) {
      val s = new Swarmer(i)
      s.initialise

      swarmers = s :: swarmers
    }
    swarmers.take(2)map(_.debug = true)
  }

  def initPreditors {

    preditors = Nil
    for (i <- 1 to Settings.numberOfPreditors) {
      val p = new Preditor(-1 * i)
      p.initialise
      preditors = p :: preditors
    }
  }

  def removePreditors {preditors = Nil}

  def step {
    clearDesires
    calculateDesiredLocations
    updatePositions
  }

  def clearDesires {
    swarmers foreach (_.clearDesires)
    preditors foreach (_.clearDesires)
  }

  def calculateDesiredLocations {

    touchLock.synchronized {
      touchPoints foreach { tp =>
        swarmers foreach (_.avoidTouch(tp))
      }
    }

    swarmers foreach (_.calculateInteractions(swarmers))
    preditors foreach (_.calculateInteractions(swarmers))

  }

  def updatePositions {
    swarmers foreach (_.updatePosition)
    preditors foreach (_.updatePosition)
  }



  val swarmerPaint = new Paint
  swarmerPaint.setAntiAlias(true)
  swarmerPaint.setARGB(255, 0, 255, 0)

  val swarmerTrailPaint = new Paint
  swarmerTrailPaint.setAntiAlias(true)
  swarmerTrailPaint.setARGB(255, 0, 255, 0)

  val preditorPaint = new Paint
  preditorPaint.setAntiAlias(true)
  preditorPaint.setARGB(255, 255, 0, 0)

  val preditorTrailPaint = new Paint
  preditorPaint.setAntiAlias(true)
  preditorPaint.setARGB(255, 255, 0, 0)

  val touchPaint = new Paint
  touchPaint.setAntiAlias(true)
  touchPaint.setARGB(155, 0, 0, 255)

  val bgPaint = new Paint
  bgPaint.setARGB(255,0, 0, 0)

  def draw(c: Canvas) {

    c.drawRect(0,0,Settings.worldSizeX, Settings.worldSizeY, bgPaint)

    touchPoints foreach { tp =>
      c.drawCircle(tp.x, tp.y, 100, touchPaint)
    }

    drawSwarmers(c)
    drawPreditors(c)
  }

  def drawSwarmers(c: Canvas) {

    swarmers foreach { s =>

      c.drawCircle(s.currentPosition.x, s.currentPosition.y, 3, swarmerPaint)

      var lineStart = s.currentPosition
      val trailAlphaStep = (255 / Settings.trailLength).toInt

      for (i <- 0 until (s.previousPositions.size)) {
        swarmerTrailPaint.setARGB(255 - (i * trailAlphaStep), 0, 255, 0)
        val p = s.previousPositions(i)

        val lineVector = new Vector( p.x - lineStart.x, p.y - lineStart.y )
        if (lineVector.magnitude < 10) {
          c.drawLine(lineStart.x, lineStart.y, p.x, p.y, swarmerTrailPaint)
        }
        lineStart = p
      }

//      if(s.debug) {
//        c.drawLine(s.currentPosition.x, s.currentPosition.y,
//          s.currentPosition.x + (s.desires.normalise * 50).x,
//          s.currentPosition.y + (s.desires.normalise * 50).y, touchPaint)
//      }
    }
  }

  def drawPreditors(c: Canvas) {
    preditors foreach { s =>
      c.drawCircle(s.currentPosition.x, s.currentPosition.y, 5, preditorPaint)

      var lineStart = s.currentPosition
      val trailAlphaStep = (255 / Settings.trailLength).toInt

      for (i <- 0 until (s.previousPositions.size)) {
        preditorTrailPaint.setARGB(255 - (i * trailAlphaStep), 255, 0, 0)
        val p = s.previousPositions(i)

        val lineVector = new Vector( p.x - lineStart.x, p.y - lineStart.y )
        if (lineVector.magnitude < 10) {
          c.drawLine(lineStart.x, lineStart.y, p.x, p.y, preditorTrailPaint)
        }
        lineStart = p
      }
    }
  }

  override def onTouch(view: View, event: MotionEvent): Boolean = {
    touchLock.synchronized {
      touchPoints = Nil
      if (event.getAction != MotionEvent.ACTION_UP)
        for (i <- 0 until event.getPointerCount)
          if (i != event.getActionIndex
                  || event.getAction != MotionEvent.ACTION_POINTER_UP)
            touchPoints = new Point(event.getX(i), event.getY(i)) :: touchPoints
    }
    true
  }

}


class SimulationThread(sim: Simulation, holder: SurfaceHolder) extends Thread {

  var running: Boolean = false

  override def run {
    while(running) {
      var c: Option[Canvas] = None
      try {
        c = Some(holder.lockCanvas)
        holder.synchronized {
          sim.step
          sim.draw(c.get)
        }
      } finally {
        c.foreach( holder.unlockCanvasAndPost(_))
      }
    }
  }

}