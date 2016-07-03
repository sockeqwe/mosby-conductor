package com.hannesdorfmann.mosby.conductor.sample.dagger

import com.hannesdorfmann.mosby.conductor.sample.create.CreateTaskController
import com.hannesdorfmann.mosby.conductor.sample.create.CreateTaskPresenter
import com.hannesdorfmann.mosby.conductor.sample.create.contactspicker.ContactsPickerPresenter
import dagger.Component

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Component(modules = arrayOf(TaskCreationModule::class),
    dependencies = arrayOf(ControllerComponent::class))
@TaskCreationScope
interface TaskCreationComponent {

  /**
   * [CreateTaskPresenter]
   */
  fun createTaskPresenter(): CreateTaskPresenter

  /**
   * Injects all required dependencies into CreateTaskController
   */
  fun inject(c: CreateTaskController)

  /**
   * [ContactsPickerPresenter]
   */
  fun contactsPickerPresenter(): ContactsPickerPresenter
}