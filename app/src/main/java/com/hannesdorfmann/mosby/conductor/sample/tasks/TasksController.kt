package com.hannesdorfmann.mosby.conductor.sample.tasks

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.daggerComponent
import com.hannesdorfmann.mosby.conductor.sample.navigator
import com.hannesdorfmann.mosby.conductor.sample.tasks.adapterdelegates.CompletedTaskAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.tasks.adapterdelegates.OpenTaskAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.tasks.adapterdelegates.TaskSectionAdapterDelegate
import com.hannesdorfmann.mosby.conductor.viewstate.lce.MvpLceViewStateController
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState

/**
 *
 * Displays a list of Tasks (open and completed)
 *
 * @author Hannes Dorfmann
 */
class TasksController : TasksView, MvpLceViewStateController<RecyclerView, List<TaskListItem>, TasksView, TasksPresenter>() {

  private lateinit var adapter: TasksAdapter

  override fun setData(data: List<TaskListItem>) {
    adapter.items = data
  }

  override fun loadData(pullToRefresh: Boolean) {
    Log.d("Test", "loadData($pullToRefresh)")
    presenter.getTasks()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(
        R.layout.controller_tasks, container, false)

    val addTask = view.findViewById(R.id.addTask)
    addTask.setOnClickListener {
      navigator.showCreateTask(addTask)
    }

    val contentView = view.findViewById(R.id.contentView) as RecyclerView

    // Setup adapter
    val delegatesManager = AdapterDelegatesManager<List<TaskListItem>>()
        .addDelegate(CompletedTaskAdapterDelegate(activity.layoutInflater, { }, {}))
        .addDelegate(OpenTaskAdapterDelegate(activity.layoutInflater, {}, {}))
        .addDelegate(TaskSectionAdapterDelegate(activity.layoutInflater))

    adapter = TasksAdapter(delegatesManager);

    contentView.adapter = adapter
    contentView.layoutManager = LinearLayoutManager(activity)

    return view
  }

  override fun createPresenter(): TasksPresenter = daggerComponent.tasksPresenter()

  override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean) = resources.getString(
      R.string.error)

  override fun createViewState(): LceViewState<List<TaskListItem>, TasksView> = RetainingLceViewState()

  override fun getData(): List<TaskListItem> = adapter.getItems()
}
