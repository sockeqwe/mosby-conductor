package com.hannesdorfmann.mosby3.conductor.sample.create.datetimepicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout

/**
 * A simple controller to pick a Date
 *
 * @author Hannes Dorfmann
 */
class TimePickerController() : Controller() {

  interface TimePickedListner {
    fun onTimePicked(hour: Int, minute: Int)
  }

  constructor(listenerController: TimePickedListner) : this() {
    if (listenerController is Controller) targetController = listenerController
    else throw IllegalArgumentException(
        "The passed TimePickedListner must extend from Controller and implement TimePickedListner")
  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(layout.controller_time_picker, container, false)
    val timePicker = view.findViewById(id.timePicker) as TimePicker
    timePicker.setOnTimeChangedListener { timePicker, hour, minute ->
      (targetController as TimePickedListner).onTimePicked(hour, minute)
    }
    return view
  }
}