package com.hannesdorfmann.mosby.conductor.sample.create

import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskDao
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import javax.inject.Inject

/**
 * Presenter to create a new Task
 *
 * @author Hannes Dorfmann
 */
class CreateTaskPresenter @Inject constructor(dao: TaskDao) : MvpBasePresenter<CreateTaskView>() {

  fun createTask(){

  }
}