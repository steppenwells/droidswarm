package com.droidswarm.app

import android.content.Context
import android.view.{SurfaceHolder, SurfaceView}
import com.droidswarm.simulation.{Simulation, SimulationThread}
import android.util.{Log, AttributeSet}

class SwarmView(val context: Context, val attrs: AttributeSet)
        extends SurfaceView(context, attrs)
        with SurfaceHolder.Callback{

  val simulation = new Simulation
  val simulationThread = new SimulationThread(simulation, getHolder)


  getHolder addCallback this
  setOnTouchListener(simulation)

  override def surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    Settings.worldSizeX = width
    Settings.worldSizeY = height
  }

  override def surfaceCreated(holder: SurfaceHolder) {
    // init sim
    simulation.initialise

    simulationThread.running = true
    simulationThread.start
  }

  override def surfaceDestroyed(holder: SurfaceHolder) {

    simulationThread.running = false
    var killed = false
    while(killed) {
      try {
        simulationThread.join
        killed = true
      } catch {
        case e: InterruptedException =>
      }
    }
  }
}