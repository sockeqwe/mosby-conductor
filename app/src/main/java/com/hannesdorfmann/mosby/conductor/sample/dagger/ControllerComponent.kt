package com.hannesdorfmann.mosby.conductor.sample.dagger

import com.hannesdorfmann.mosby.conductor.sample.create.CreateTaskPresenter
import com.hannesdorfmann.mosby.conductor.sample.create.contactspicker.ContactsPickerPresenter
import com.hannesdorfmann.mosby.conductor.sample.navigation.Navigator
import com.hannesdorfmann.mosby.conductor.sample.tasks.TasksPresenter
import dagger.Component

/**
 * Dagger Component for controllers
 *
 * @author Hannes Dorfmann
 */
@Component(modules = arrayOf(ControllerModule::class),
    dependencies = arrayOf(ApplicationComponent::class))
@ControllerScope
interface ControllerComponent {

  /**
   * [Navigator]
   */
  fun navigator(): Navigator

  /**
   * [TasksPresenter]
   */
  fun tasksPresenter(): TasksPresenter

  /**
   * [CreateTaskPresenter]
   */
  fun createTaskPresenter(): CreateTaskPresenter

  /**
   * [ContactsPickerPresenter]
   */
  fun contactsPickerPresenter() : ContactsPickerPresenter
}