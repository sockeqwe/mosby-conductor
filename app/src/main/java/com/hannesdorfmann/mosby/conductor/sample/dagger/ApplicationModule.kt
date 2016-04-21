package com.hannesdorfmann.mosby.conductor.sample.dagger

import android.content.Context
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.ContactsLoader
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.ContactsLoaderImpl
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskDao
import com.hannesdorfmann.mosby.conductor.sample.model.tasks.TaskDaoImpl
import com.hannesdorfmann.sqlbrite.dao.DaoManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 * Providing all dependencies for the whole application
 * @author Hannes Dorfmann
 */
@Module
class ApplicationModule(private val applicationContext: Context) {

  private val taskDao: TaskDao

  init {
    taskDao = TaskDaoImpl()

    DaoManager.with(applicationContext)
        .databaseName("Tasks.db")
        .version(1)
        .add(taskDao)
        .build()
  }

  @Singleton
  @Provides
  fun provideTaskDao() = taskDao

  @Singleton
  @ApplicationContext
  @Provides
  fun provideApplicationContext() = applicationContext

  @Singleton
  @Provides
  fun provideContactsLoader(): ContactsLoader = ContactsLoaderImpl(applicationContext)

}