package com.hannesdorfmann.mosby3.conductor.sample.create.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import org.threeten.bp.ZonedDateTime

/**
 * A simple controller to pick a Date
 *
 * @author Hannes Dorfmann
 */
class DatePickerController() : Controller() {

  interface DatePickedListner {
    fun onDatePicked(year: Int, month: Int, day: Int)
  }

  constructor(listenerController: DatePickedListner) : this() {
    if (listenerController is Controller) {
      targetController = listenerController
    }
    else throw IllegalArgumentException(
        "The passed DatePickedListener must extend from Controller and implement DatePickedListener")
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
    val view = inflater.inflate(layout.controller_date_picker, container, false)
    val datePicker = view.findViewById<DatePicker>(id.datePicker) as DatePicker
    val now = ZonedDateTime.now()
    datePicker.minDate = now.toEpochSecond() * 1000L

    datePicker.init(now.year, now.monthValue, now.dayOfMonth) { calendarView, year, month, day ->
      (targetController as DatePickedListner).onDatePicked(year, month, day)
    }

    return view
  }
}