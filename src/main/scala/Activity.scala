package com.steppenwells.droidswarm

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.droidswarm_layout)

    val swarmView = findViewById(R.id.swarm)
    
//    setContentView(new TextView(this) {
//      setText("Arsenal are shit!")
//    })
  }
}