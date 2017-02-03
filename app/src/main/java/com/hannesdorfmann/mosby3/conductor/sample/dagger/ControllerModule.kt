package com.hannesdorfmann.mosby3.conductor.sample.dagger

import com.hannesdorfmann.mosby3.conductor.sample.navigation.Navigator
import dagger.Module
import dagger.Provides

/**
 *
 * Module for Controller
 *
 * @author Hannes Dorfmann
 */
@Module
class ControllerModule(private val navigator: Navigator) {

  @Provides
  @ControllerScope
  fun provideNavigator() = navigator
}