package com.hannesdorfmann.mosby.conductor.sample.dagger

import com.hannesdorfmann.mosby.conductor.sample.model.contacts.ContactsLoader
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskDao
import dagger.Component
import javax.inject.Singleton

/**
 * Proide Application wide dependencies
 *
 * @author Hannes Dorfmann
 */
@Component(modules = arrayOf(ApplicationModule::class))
@Singleton
interface ApplicationComponent {

  fun taskDao(): TaskDao

  fun contactsLoader() : ContactsLoader
}