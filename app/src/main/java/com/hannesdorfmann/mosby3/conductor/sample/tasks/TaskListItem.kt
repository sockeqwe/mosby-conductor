package com.hannesdorfmann.mosby3.conductor.sample.tasks

import androidx.annotation.StringRes

/**
 * Common "PresentationModel" interface for elements that can be displayed in [TaskListItem]
 * @author Hannes Dorfmann
 */
interface TaskListItem {
  val id: Long
}

/**
 * Represents a section in the TaskListView. There are only two: Open Tasks (not done yet) and done Tasks
 */
data class TasksSection(override val id: Long, @StringRes val titleRes: Int) : TaskListItem

