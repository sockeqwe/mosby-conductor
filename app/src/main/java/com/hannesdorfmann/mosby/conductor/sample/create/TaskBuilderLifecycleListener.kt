package com.hannesdorfmann.mosby.conductor.sample.create

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskBuilder

/**
 * This is just some kind of "lifecycle aware container" for [TaskBuilder] because [TaskBuilder] is lifecycle aware
 *
 * @author Hannes Dorfmann
 */
class TaskBuilderLifecycleListener(private val taskBuilder: TaskBuilder) : Controller.LifecycleListener() {

  override fun onSaveInstanceState(controller: Controller, outState: Bundle) {
    taskBuilder.saveInstanceState(outState)
  }

  override fun onRestoreInstanceState(controller: Controller, savedInstanceState: Bundle) {
    taskBuilder.restoreFromBundle(savedInstanceState)
  }

}