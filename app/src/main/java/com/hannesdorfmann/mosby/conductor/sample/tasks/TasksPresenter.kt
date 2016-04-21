package com.hannesdorfmann.mosby.conductor.sample.tasks

import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskDao
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Tasks Presenter. Responsible to interact with [TasksView] to display a list of tasks
 *
 * @author Hannes Dorfmann
 */
class TasksPresenter @Inject constructor(private val dao: TaskDao) : MvpBasePresenter<TasksView>() {

  private lateinit var subscription: Subscription

  /**
   * Loads the tasks and keep subscribed, so we get automatically updated when we add or remove an item
   */
  fun getTasks() {

    view?.showLoading(false)

    subscription = dao.getTasks()
        .map {
          val items = ArrayList<TaskListItem>()
          val (done, open) = it.partition { task -> task.completed }
          if (open.isNotEmpty()) {
            items.add(TasksSection(-1, R.string.tasks_open))
            items.addAll(open)
          }
          if (done.isNotEmpty()) {
            items.add(TasksSection(-2, R.string.tasks_done))
            items.addAll(done)
          }
          items
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe ({ // onNext
          list ->
          view?.setData(list)
          view?.showContent()
        }, { // onError
          view?.showError(it, false)
        }
        )
  }

  override fun detachView(retainInstance: Boolean) {
    super.detachView(retainInstance)
    if (!retainInstance) {
      subscription.unsubscribe()
    }
  }

}