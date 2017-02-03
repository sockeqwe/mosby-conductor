package com.hannesdorfmann.mosby3.conductor.sample.tasks

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.daggerComponent
import com.hannesdorfmann.mosby3.conductor.sample.navigator
import com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates.CompletedTaskAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates.OpenTaskAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates.TaskSectionAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.viewstate.lce.MvpLceViewStateController
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.R.string

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
        layout.controller_tasks, container, false)

    val addTask = view.findViewById(id.addTask)
    addTask.setOnClickListener {
      navigator.showCreateTask(addTask)
    }

    val contentView = view.findViewById(id.contentView) as RecyclerView

    // Setup adapter
    val delegatesManager = AdapterDelegatesManager<List<TaskListItem>>()
        .addDelegate(CompletedTaskAdapterDelegate(activity!!.layoutInflater, { }, {}))
        .addDelegate(OpenTaskAdapterDelegate(activity!!.layoutInflater, {}, {}))
        .addDelegate(TaskSectionAdapterDelegate(activity!!.layoutInflater))

    adapter = TasksAdapter(delegatesManager);

    contentView.adapter = adapter
    contentView.layoutManager = LinearLayoutManager(activity)

    return view
  }

  override fun createPresenter(): TasksPresenter = daggerComponent.tasksPresenter()

  override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean) = resources!!.getString(
      string.error)

  override fun createViewState(): LceViewState<List<TaskListItem>, TasksView> = RetainingLceViewState()

  override fun getData(): List<TaskListItem> = adapter.getItems()
}
