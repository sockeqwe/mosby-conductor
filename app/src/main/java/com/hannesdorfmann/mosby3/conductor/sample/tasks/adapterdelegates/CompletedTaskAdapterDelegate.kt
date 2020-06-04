package com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.model.tasks.Task
import com.hannesdorfmann.mosby3.conductor.sample.tasks.TaskListItem
import com.hannesdorfmann.mosby3.conductor.sample.tasks.adapterdelegates.CompletedTaskAdapterDelegate.CompletedTaskViewHolder
import kotterknife.bindView

/**
 * AdapterDelegate that represents an Task that is open (not completed yet)
 * @author Hannes Dorfmann
 */
class CompletedTaskAdapterDelegate(
    private val inflater: LayoutInflater,
    private val clickListener: (Task) -> Unit,
    val checkListener: (Task) -> Unit) : AbsListItemAdapterDelegate<Task, TaskListItem, CompletedTaskViewHolder>() {


  override fun isForViewType(item: TaskListItem, items: MutableList<TaskListItem>?,
      position: Int): Boolean = item is Task && item.completed

  override fun onCreateViewHolder(parent: ViewGroup) = CompletedTaskViewHolder(
      inflater.inflate(layout.item_task, parent, false))

  override fun onBindViewHolder(item: Task, vh: CompletedTaskViewHolder) {
    vh.task = item
    vh.checkbox.isChecked = true
    vh.title.text = item.title
  }

  inner class CompletedTaskViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    lateinit var task: Task
    val checkbox by bindView<CheckBox>(id.checkbox)
    val title by bindView<TextView>(id.title)

    init {
      checkbox.setOnCheckedChangeListener { a, b -> checkListener(task) }
      title.setOnClickListener { clickListener(task) }
      title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
  }

}
