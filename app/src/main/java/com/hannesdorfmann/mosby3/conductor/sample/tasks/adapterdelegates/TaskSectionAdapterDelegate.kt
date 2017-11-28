package com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.tasks.TaskListItem
import com.hannesdorfmann.mosby3.conductor.sample.tasks.TasksSection
import com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates.TaskSectionAdapterDelegate.TasksSectionViewHolder

/**
 * Represents a Section item in the list
 * @author Hannes Dorfmann
 */
class TaskSectionAdapterDelegate(private val inflater: LayoutInflater) : AbsListItemAdapterDelegate<TasksSection, TaskListItem, TasksSectionViewHolder>() {

  override fun isForViewType(item: TaskListItem, items: MutableList<TaskListItem>?,
      position: Int) = item is TasksSection

  override fun onCreateViewHolder(parent: ViewGroup) = TasksSectionViewHolder(
      inflater.inflate(layout.item_task_section, parent, false))

  override fun onBindViewHolder(item: TasksSection, viewHolder: TasksSectionViewHolder) {
    viewHolder.title.setText(item.titleRes)
  }

  inner class TasksSectionViewHolder(v: View) : ViewHolder(v) {
    val title by bindView<TextView>(id.title)
  }
}