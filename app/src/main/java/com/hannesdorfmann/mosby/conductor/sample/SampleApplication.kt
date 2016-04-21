package com.hannesdorfmann.mosby.conductor.sample

import android.app.Application
import android.content.Context
import com.hannesdorfmann.mosby.conductor.sample.dagger.ApplicationComponent
import com.hannesdorfmann.mosby.conductor.sample.dagger.ApplicationModule
import com.hannesdorfmann.mosby.conductor.sample.dagger.DaggerApplicationComponent

/**
 * Application class to integrate dagger
 * @author Hannes Dorfmann
 */
class SampleApplication : Application() {

  companion object {
    private var component: ApplicationComponent? = null;
    fun getComponent(c: Context): ApplicationComponent {
      if (component == null) {
        component = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(c.applicationContext)).build()
      }
      return component!!
    }
  }

}