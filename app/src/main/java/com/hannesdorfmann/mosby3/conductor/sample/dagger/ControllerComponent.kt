package com.hannesdorfmann.mosby3.conductor.sample.dagger

import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.ContactsLoader
import com.hannesdorfmann.mosby3.conductor.sample.model.tasks.TaskDao
import com.hannesdorfmann.mosby3.conductor.sample.navigation.Navigator
import com.hannesdorfmann.mosby3.conductor.sample.tasks.TasksPresenter
import com.squareup.picasso.Picasso
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

  fun picasso(): Picasso

  fun taskDao(): TaskDao

  fun contactsLoader(): ContactsLoader

  /**
   * [Navigator]
   */
  fun navigator(): Navigator

  /**
   * [TasksPresenter]
   */
  fun tasksPresenter(): TasksPresenter

}