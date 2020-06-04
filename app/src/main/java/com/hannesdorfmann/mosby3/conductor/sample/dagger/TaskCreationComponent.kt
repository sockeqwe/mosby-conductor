package com.hannesdorfmann.mosby3.conductor.sample.dagger

import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskController
import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskPresenter
import com.hannesdorfmann.mosby3.conductor.sample.create.contactspicker.ContactsPickerPresenter
import dagger.Component

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Component(modules = [TaskCreationModule::class],
    dependencies = [ControllerComponent::class])
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