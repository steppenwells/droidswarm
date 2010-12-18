package com.steppenwells.droidswarm

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView
import android.view.{MenuItem, Menu}
import com.droidswarm.app.{Settings, SwarmView}

class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.droidswarm_layout)

    val swarmView = findViewById(R.id.swarm)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.reset => {
        val swarmView = findViewById(R.id.swarm).asInstanceOf[SwarmView]
        swarmView.simulation.initialise
        true
      }
      case R.id.touch => {
        Settings.touchDirection = Settings.touchDirection * -1
        true
      }
      case _ => super.onOptionsItemSelected(item)
    }
  }

}