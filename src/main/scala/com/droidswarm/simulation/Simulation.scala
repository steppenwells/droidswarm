package com.droidswarm.simulation

import com.droidswarm.app.Settings
import android.util.Log
import android.graphics.{Path, ColorFilter, Paint, Canvas}
import android.view.View.OnTouchListener
import android.view.{View, MotionEvent, SurfaceHolder}

class Simulation extends OnTouchListener {
  var swarmers: List[Swarmer] = Nil

  var touchPoints: List[Point] = Nil
  val touchLock: String = ""

  def initialise {

    for (i <- 1 to Settings.numberOfSwarmers) {
      val s = new Swarmer(i)
      s.initialise

      swarmers = s :: swarmers
    }
    swarmers.take(2)map(_.debug = true)
//    Log.i("sim", "init "+swarmers.size+" swarmers" )
  }

  def step {
//    Log.i("sim", "starting simulation step" )
    clearDesires
    calculateDesiredLocations
    updatePositions
//    Log.i("sim", "done simulation step" )
  }

  def clearDesires {
    swarmers foreach (_.clearDesires)
  }

  def calculateDesiredLocations {

    touchLock.synchronized {
      touchPoints foreach { tp =>
        swarmers foreach (_.avoidTouch(tp))
      }
    }

    swarmers foreach {
      _.calculateInteractions(swarmers)
    }
  }

  def updatePositions {
    swarmers foreach (_.updatePosition)
  }


  def draw(c: Canvas) {
//    Log.i("sim", "drawing state" )
    val swarmerPaint = new Paint
    swarmerPaint.setAntiAlias(true)
    swarmerPaint.setARGB(255, 255, 0, 0)

    val trailPaint = new Paint
    trailPaint.setAntiAlias(true)
    trailPaint.setARGB(255, 255, 0, 0)

    val touchPaint = new Paint
    touchPaint.setAntiAlias(true)
    touchPaint.setARGB(155, 0, 0, 255)

    val bgPaint = new Paint
    bgPaint.setARGB(255,255,255,255)
    c.drawRect(0,0,Settings.worldSizeX, Settings.worldSizeY, bgPaint)

    touchPoints foreach { tp =>


      c.drawCircle(tp.x, tp.y, 100, touchPaint)
    }


    swarmers foreach { s =>

      c.drawCircle(s.currentPosition.x, s.currentPosition.y, 3, swarmerPaint)

      var lineStart = s.currentPosition
      val trailAlphaStep = (255 / Settings.trailLength).toInt
      for (i <- 0 until (s.previousPositions.size)) {
        trailPaint.setARGB(255 - (i * trailAlphaStep), 255, 0, 0)
        val p = s.previousPositions(i)

        val lineVector = new Vector( p.x - lineStart.x, p.y - lineStart.y )
        if (lineVector.magnitude < 10) {
          c.drawLine(lineStart.x, lineStart.y, p.x, p.y, trailPaint)
        }
        lineStart = p
      }
//      s.previousPositions.foreach { p =>
//        val lineVector = new Vector( p.x - lineStart.x, p.y - lineStart.y )
//        if (lineVector.magnitude < 10) {
//          c.drawLine(lineStart.x, lineStart.y, p.x, p.y, trailPaint)
//        }
//        lineStart = p
//      }
    }


//    Log.d("swarmer", "drawing a swarmer at "+ swarmers.head.currentPosition)
//    Log.i("sim", "done drawing" )
  }

  override def onTouch(view: View, event: MotionEvent): Boolean = {
    Log.i("TOUCH", event.toString)
    touchLock.synchronized {
      touchPoints = Nil
      for (i <- 0 until event.getPointerCount)
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