package com.hannesdorfmann.mosby3.conductor.sample.dagger

import com.hannesdorfmann.mosby3.conductor.sample.create.TaskBuilderLifecycleListener
import com.hannesdorfmann.mosby3.conductor.sample.model.tasks.TaskBuilder
import dagger.Module
import dagger.Provides

/**
 * Dagger Module for components
 *
 * @author Hannes Dorfmann
 */
@Module
class TaskCreationModule {


  @Provides
  @TaskCreationScope
  fun provideTaskBuilder() = TaskBuilder()

  @Provides
  @TaskCreationScope
  fun provideTaskBuilderLifecycleListener(tb: TaskBuilder) = TaskBuilderLifecycleListener(tb)
}