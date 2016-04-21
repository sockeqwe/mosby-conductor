package com.hannesdorfmann.mosby.conductor.sample.create

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState

/**
 * ViewState for [CreateTaskView]
 * @author Hannes Dorfmann
 */
class CreateTaskViewState : ViewState<CreateTaskView> {

  enum class State {
    SHOW_FORM, SHOW_LOADING, SHOW_TASK_CREATED
  }

  private var state = State.SHOW_FORM

  override fun apply(view: CreateTaskView, retained: Boolean) = when (state) {
    State.SHOW_FORM -> view.showForm()
    State.SHOW_LOADING -> view.showLoading()
    State.SHOW_TASK_CREATED -> view.showTaskCreated()
  }

  fun setShowForm() {
    state = State.SHOW_FORM
  }

  fun setShowLoading() {
    state = State.SHOW_LOADING
  }

  fun setShowTaskCreated() {
    state = State.SHOW_TASK_CREATED
  }
}