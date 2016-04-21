package com.hannesdorfmann.mosby.conductor.sample.tasks.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.tasks.TaskListItem
import com.hannesdorfmann.mosby.conductor.sample.tasks.TasksSection

/**
 * Represents a Section item in the list
 * @author Hannes Dorfmann
 */
class TaskSectionAdapterDelegate(private val inflater: LayoutInflater) : AbsListItemAdapterDelegate<TasksSection, TaskListItem, TaskSectionAdapterDelegate.TasksSectionViewHolder>() {

  override fun isForViewType(item: TaskListItem, items: MutableList<TaskListItem>?,
      position: Int) = item is TasksSection

  override fun onCreateViewHolder(parent: ViewGroup) = TasksSectionViewHolder(
      inflater.inflate(R.layout.item_task_section, parent, false))

  override fun onBindViewHolder(item: TasksSection, viewHolder: TasksSectionViewHolder) {
    viewHolder.title.setText(item.titleRes)
  }

  inner class TasksSectionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val title by bindView<TextView>(R.id.title)
  }
}