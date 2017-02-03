package com.hannesdorfmann.mosby3.conductor.sample

import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.mosby3.conductor.sample.dagger.ControllerComponent
import com.hannesdorfmann.mosby3.conductor.sample.dagger.ControllerModule
import com.hannesdorfmann.mosby3.conductor.sample.dagger.DaggerControllerComponent
import com.hannesdorfmann.mosby3.conductor.sample.SampleApplication.Companion
import com.hannesdorfmann.mosby3.conductor.sample.navigation.Navigator
import com.hannesdorfmann.mosby3.conductor.sample.navigation.PhoneNavigator
import com.hannesdorfmann.mosby3.conductor.sample.navigation.TabletNavigator

/**
 * Some useful extension functions
 * @author Hannes Dorfmann
 */

fun Controller.isTablet() = false // TODO implement

val Controller.daggerComponent: ControllerComponent
  get() = DaggerControllerComponent
      .builder()
      .applicationComponent(SampleApplication.getComponent(applicationContext!!))
      .controllerModule(
          ControllerModule(if (isTablet()) TabletNavigator(router) else PhoneNavigator(router)))
      .build()


val Controller.navigator: Navigator
  get() = daggerComponent.navigator()


