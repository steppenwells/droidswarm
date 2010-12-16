package com.droidswarm.simulation

class Simulation {
  var swarmers: List[Swarmer] = Nil

  def initialise {

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