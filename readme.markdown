Droid Swarm
===========

A BOID simulation for your android device



BOIDs
=====

BOIDS is an artificial life program which simulates flocking behaviour of birds.

Each simulated boid agent interacts with all the other agents and applies simple rules:

* *cohesion:* steer towards the average position of the other boids in the local area.

* *separation:* Avoid colliding with close boids.

* *alignment:* head in the same direction as local bodies

By applying these rules (so long as they are balanced properly) produces emergent flocking behaviour.

Droid
=====

The application is a boid simulation for the android platform

* written in 100% pure scala
(with the exception of the xml to configure the app and the generated R.java file)

* built using sbt and the androids plugin

* tested on a Samsung galaxy tab device.

* pseudo toroidal world - the boids move on a torus but the intention simulation does not wrap. This spices up the visualisation by providing a mechanism for splitting up the flock. 

The result, pretty dots moving round the screen.


Beyond the basic Sim
====================

The basic simulation is interesting but the touch screen on android devices cries out to make the simulation interactive.

The simulation supports multitouch to allow you to interact with the simulation. The swarm will try to avoid your touches by default, or you can configure it to follow your pressed via the application menu.

To further spice things up you can also add predators via the application menu. This adds two more boids to the simulation that will attempt to eat the normal boids, the normal bodies obviously don't want this to happen and so try to flee the predators.
