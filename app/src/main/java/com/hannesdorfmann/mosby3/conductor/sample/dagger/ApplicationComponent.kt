package com.hannesdorfmann.mosby3.conductor.sample.dagger

import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.ContactsLoader
import com.hannesdorfmann.mosby3.conductor.sample.model.tasks.TaskDao
import com.squareup.picasso.Picasso
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

  fun contactsLoader(): ContactsLoader

  fun picasso(): Picasso
}