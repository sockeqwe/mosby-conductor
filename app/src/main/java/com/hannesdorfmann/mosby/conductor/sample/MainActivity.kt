package com.hannesdorfmann.mosby.conductor.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.hannesdorfmann.mosby.conductor.sample.tasks.TasksController

class MainActivity : AppCompatActivity() {

  private lateinit var router: Router

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val container = findViewById(R.id.container) as ViewGroup

    router = Conductor.attachRouter(this, container, savedInstanceState)
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(TasksController()))
    }
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }
}
