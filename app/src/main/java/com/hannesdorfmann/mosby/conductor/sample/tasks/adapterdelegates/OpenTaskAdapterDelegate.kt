package com.hannesdorfmann.mosby.conductor.sample.tasks.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import butterknife.bindView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.Task
import com.hannesdorfmann.mosby.conductor.sample.tasks.TaskListItem


/**
 * AdapterDelegate that represents an Task that is open (not completed yet)
 * @author Hannes Dorfmann
 */
class OpenTaskAdapterDelegate(
    private val inflater: LayoutInflater,
    private val clickListener: (Task) -> Unit,
    val checkListener: (Task) -> Unit) : AbsListItemAdapterDelegate<Task, TaskListItem, OpenTaskAdapterDelegate.OpenTaskViewHolder>() {


  override fun isForViewType(item: TaskListItem, items: MutableList<TaskListItem>?,
      position: Int): Boolean = item is Task && !item.completed

  override fun onCreateViewHolder(parent: ViewGroup) = OpenTaskViewHolder(
      inflater.inflate(R.layout.item_task, parent, false))

  override fun onBindViewHolder(item: Task, vh: OpenTaskViewHolder) {
    vh.task = item
    vh.checkbox.isChecked = false
    vh.title.text = item.title
  }

  inner class OpenTaskViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    lateinit var task: Task
    val checkbox by bindView<CheckBox>(R.id.checkbox)
    val title by bindView<TextView>(R.id.title)

    init {
      checkbox.setOnCheckedChangeListener { a, b -> checkListener(task) }
      title.setOnClickListener { clickListener(task) }
    }
  }

}
