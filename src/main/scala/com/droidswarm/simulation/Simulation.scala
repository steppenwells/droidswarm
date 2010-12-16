package com.droidswarm.simulation

import com.droidswarm.app.Settings

class Simulation {
  var swarmers: List[Swarmer] = Nil

  def initialise {

    for (i <- 1 to Settings.numberOfSwarmers) {
      val s = new Swarmer(i)
      s.initialise

      swarmers = s :: swarmers
    }
  }

  def step {
    clearDesires
    calculateDesiredLocations
    updatePositions
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
}


class SimulationThread(sim: Simulation) extends Thread {

  var running: Boolean = false

}