package com.hannesdorfmann.mosby3.conductor.sample.navigation

import android.os.Build
import android.os.Build.VERSION
import android.view.View
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.hannesdorfmann.mosby3.conductor.sample.create.CreateTaskController
import com.hannesdorfmann.mosby3.conductor.sample.navigation.changehandlers.CreateTaskCircularRevealChangeHandler

/**
 *
 * Responsible to navigate from one screen to another
 *
 * @author Hannes Dorfmann
 */
interface Navigator {

  val router: Router

  /**
   * Show the create task
   */
  fun showCreateTask(fab: View) {
    // TODO we could also provide a NavigarotApi21 instance
    val handler = if (VERSION.SDK_INT < 21) {
      HorizontalChangeHandler()
    } else {
      CreateTaskCircularRevealChangeHandler()
    }



    router.pushController(RouterTransaction.with(CreateTaskController())
        .pushChangeHandler(handler)
        .popChangeHandler(handler)
    )
  }

}