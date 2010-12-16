package com.droidswarm.simulation

import com.droidswarm.app.Settings
import android.view.SurfaceHolder
import android.graphics.{ColorFilter, Paint, Canvas}
import android.util.Log

class Simulation {
  var swarmers: List[Swarmer] = Nil

  def initialise {

    for (i <- 1 to Settings.numberOfSwarmers) {
      val s = new Swarmer(i)
      s.initialise

      swarmers = s :: swarmers
    }
    swarmers.head.debug = true
    Log.i("sim", "init "+swarmers.size+" swarmers" )
  }

  def step {
    Log.i("sim", "starting simulation step" )
    clearDesires
    calculateDesiredLocations
    updatePositions
    Log.i("sim", "done simulation step" )
  }

  def clearDesires {
    swarmers foreach (_.clearDesires)
  }

  def calculateDesiredLocations {
    swarmers foreach {
      _.calculateInteractions(swarmers)
    }
  }

  def updatePositions {
    swarmers foreach (_.updatePosition)
  }


  def draw(c: Canvas) {
    Log.i("sim", "drawing state" )
    val swarmerPaint = new Paint
    swarmerPaint.setAntiAlias(true)
    swarmerPaint.setARGB(255, 255, 0, 0)

    val bgPaint = new Paint
    bgPaint.setARGB(255,255,255,255)
    c.drawRect(0,0,Settings.worldSizeX, Settings.worldSizeY, bgPaint)

    swarmers foreach { s =>

      c.drawCircle(s.currentPosition.x, s.currentPosition.y, 2, swarmerPaint)
    }
    Log.d("swarmer", "drawing a swarmer at "+ swarmers.head.currentPosition)
    Log.i("sim", "done drawing" )
  }
}


class SimulationThread(sim: Simulation, holder: SurfaceHolder) extends Thread {

  var running: Boolean = false

  override def run {
    while(running) {
      var c: Option[Canvas] = None
      try {
        c = Some(holder.lockCanvas)
        Log.i("sim", "in loop" )
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