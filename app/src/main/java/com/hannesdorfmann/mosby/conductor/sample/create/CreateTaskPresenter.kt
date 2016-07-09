package com.hannesdorfmann.mosby.conductor.sample.create

import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskBuilder
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskDao
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import rx.Subscription
import javax.inject.Inject

/**
 * Presenter to create a new Task
 *
 * @author Hannes Dorfmann
 */
class CreateTaskPresenter @Inject constructor(private val dao: TaskDao, private val taskBuilder: TaskBuilder) : MvpBasePresenter<CreateTaskView>() {
  private lateinit var taskBuilderSubscription: Subscription

  override fun attachView(view: CreateTaskView) {
    super.attachView(view)
    taskBuilderSubscription = taskBuilder.observable.subscribe({ getView()?.setTaskSnapshot(it) })
  }

  override fun detachView(retainInstance: Boolean) {
    super.detachView(retainInstance)
    taskBuilderSubscription.unsubscribe()
  }

  fun setTaskTitle(title: String) {
    taskBuilder.setTitle(title)
  }

  fun setTaskDescription(description: String) {
    taskBuilder.setDescription(description)
  }

  fun saveTask() {

  }

}