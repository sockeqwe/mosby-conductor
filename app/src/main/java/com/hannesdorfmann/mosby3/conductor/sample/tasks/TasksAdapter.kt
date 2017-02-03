package com.hannesdorfmann.mosby3.conductor.sample.tasks

import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates2.ListDelegationAdapter

/**
 * Responsible to display a lis of [TaskListItem] by using AdapterDelegats library
 * https://github.com/sockeqwe/AdapterDelegates
 *
 * @author Hannes Dorfmann
 */
class TasksAdapter(manager: AdapterDelegatesManager<List<TaskListItem>>) : ListDelegationAdapter<List<TaskListItem>>(
    manager) {

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int): Long = items[position].id

}
