package com.hannesdorfmann.mosby.conductor.sample.navigation.changehandlers

import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.ControllerChangeHandler

/**
 * Responsible to change Controllers for [com.hannesdorfmann.mosby.conductor.sample.create.contactspicker.ContactsPickerController]
 *
 * @author Hannes Dorfmann
 */
class ContactsPickerChaneHandler : ControllerChangeHandler() {

  override fun performChange(container: ViewGroup, from: View?, to: View?, isPush: Boolean,
      changeListener: ControllerChangeCompletedListener) {

    val transition = AutoTransition()

    transition.addListener(object : Transition.TransitionListener {

      override fun onTransitionEnd(transition: Transition?) {
        changeListener.onChangeCompleted()
      }

      override fun onTransitionResume(transition: Transition?) {
      }

      override fun onTransitionPause(transition: Transition?) {
      }

      override fun onTransitionCancel(transition: Transition?) {
        changeListener.onChangeCompleted()
      }

      override fun onTransitionStart(transition: Transition?) {
      }
    })

    // Change Views
    if (isPush) {
      if (to != null) {
        val parentContainer = to.parent as ViewGroup
        TransitionManager.beginDelayedTransition(parentContainer.parent as ViewGroup, transition)
        parentContainer.visibility = View.VISIBLE
      }

    } else {
      if (from != null) {
        val parentContainer = from.parent as ViewGroup
        TransitionManager.beginDelayedTransition(parentContainer.parent as ViewGroup, transition)
        parentContainer.visibility = View.GONE
      }
    }

  }
}