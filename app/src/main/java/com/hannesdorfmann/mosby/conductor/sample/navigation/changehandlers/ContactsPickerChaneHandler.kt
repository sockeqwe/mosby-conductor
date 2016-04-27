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
      override fun onTransitionStart(transition: Transition) {
      }

      override fun onTransitionEnd(transition: Transition) {
        changeListener.onChangeCompleted()
      }

      override fun onTransitionCancel(transition: Transition) {
        changeListener.onChangeCompleted()
      }

      override fun onTransitionPause(transition: Transition) {
      }

      override fun onTransitionResume(transition: Transition) {
      }
    })

    TransitionManager.beginDelayedTransition(container.parent as ViewGroup, transition)
    if (from != null) {
      container.removeView(from)
    }
    if (to != null) {
      container.addView(to)
    }
  }
}