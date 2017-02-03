package com.hannesdorfmann.mosby3.conductor.sample.create

import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState
import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskViewState.State.SHOW_FORM
import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskViewState.State.SHOW_LOADING
import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskViewState.State.SHOW_TASK_CREATED

/**
 * ViewState for [CreateTaskView]
 * @author Hannes Dorfmann
 */
class CreateTaskViewState : ViewState<CreateTaskView> {

  enum class State {
    SHOW_FORM, SHOW_LOADING, SHOW_TASK_CREATED
  }

  private var state = SHOW_FORM

  override fun apply(view: CreateTaskView, retained: Boolean) = when (state) {
    SHOW_FORM -> view.showForm()
    SHOW_LOADING -> view.showLoading()
    SHOW_TASK_CREATED -> view.showTaskCreated()
  }

  fun setShowForm() {
    state = SHOW_FORM
  }

  fun setShowLoading() {
    state = SHOW_LOADING
  }

  fun setShowTaskCreated() {
    state = SHOW_TASK_CREATED
  }
}