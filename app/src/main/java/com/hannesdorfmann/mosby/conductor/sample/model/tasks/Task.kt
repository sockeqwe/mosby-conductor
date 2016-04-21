package com.hannesdorfmann.mosby.conductor.sample.model.tasks

import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby.conductor.sample.tasks.TaskListItem
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable

/**
 * Represents a single Task
 * @author Hannes Dorfmann
 */
// TODO use auto value
// TODO I know TaskListItem is part of the presentation layer and shouldn't be in Model layer. It's prototyping.
@ObjectMappable
class Task : TaskListItem {


  @Column(TaskDaoImpl.COL_ID)
  override var id: Long = 0


  @Column(TaskDaoImpl.COL_TITLE)
  lateinit var title: String

  @Column(TaskDaoImpl.COL_DESCRIPTION)
  var description: String? = null


  @Column(TaskDaoImpl.COL_COMPLETED)
  var completed: Boolean = false

  // For internal usage only
  @Column(TaskDaoImpl.COL_CONTACTS)
  var _contacts: String = ""

  val contacts: List<Contact>
    get() = _contacts.split(Regex(";")).map { Contact(it) }.toList()
}