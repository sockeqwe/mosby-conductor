package com.hannesdorfmann.mosby.conductor.sample.create

import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskBuilder
import com.hannesdorfmann.mosby.mvp.MvpView

/**
 * Create a new Task
 * @author Hannes Dorfmann
 */
interface CreateTaskView : MvpView {

  /**
   * Show the input form
   */
  fun showForm()

  /**
   * Show the loading indicator
   */
  fun showLoading()

  /**
   * Show that the task has been created successfully
   */
  fun showTaskCreated()
  
  /**
   * Set the current [com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskBuilder.TaskSnapshot]
   * data object that displays title, description etc.
   */
  fun setTaskSnapshot(taskSnapshot: TaskBuilder.TaskSnapshot)

}