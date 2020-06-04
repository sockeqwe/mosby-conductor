package com.hannesdorfmann.mosby3.conductor.sample.create.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager.RouterPagerAdapter
import com.hannesdorfmann.mosby3.conductor.sample.R
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.R.string
import com.hannesdorfmann.mosby3.conductor.sample.create.datetimepicker.DatePickerController.DatePickedListner
import com.hannesdorfmann.mosby3.conductor.sample.create.datetimepicker.TimePickerController.TimePickedListner
import me.relex.circleindicator.CircleIndicator

/**
 * A simple Controller for picking a Date and a Time when a Task has to be done.
 *
 * @author Hannes Dorfmann
 */
class DateTimePickerController : Controller(), DatePickedListner, TimePickedListner {

    private val KEY_YEAR = "year"
    private val KEY_MONTH = "month"
    private val KEY_DAY = "day"
    private val KEY_HOUR = "hour"
    private val KEY_MINUTE = "minute"

    private var day = -1
    private var month = -1
    private var year = -1
    private var hour = -1
    private var minute = -1


    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var viewPager: ViewPager
    private var pagerAdapter: RouterPagerAdapter

    init {

        pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {

                    when (position) {

                        0 -> router.setRoot(RouterTransaction.with(DatePickerController(this@DateTimePickerController)))

                        1 -> router.setRoot(RouterTransaction.with(TimePickerController(this@DateTimePickerController)))

                        else -> throw IllegalArgumentException(
                                "No controller set for page at index $position in ViewPager")


                    }

                }

            }

            override fun getCount(): Int = 2

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflater.inflate(layout.controller_datetime_picker, container, false)
        viewPager = view.findViewById<ViewPager>(id.viewPager) as ViewPager
        val circleIndicator = view.findViewById<CircleIndicator>(id.circleIndicator) as CircleIndicator
        backButton = view.findViewById<Button>(id.backButton) as Button
        nextButton = view.findViewById<Button>(id.nextFinishButton) as Button

        // Init listeners etc.
        viewPager.adapter = pagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                setupButtons(position)
            }
        })

        circleIndicator.setViewPager(viewPager)
        backButton.setOnClickListener { viewPager.setCurrentItem(0, true) }
        nextButton.setOnClickListener {
            if (viewPager.currentItem == pagerAdapter.count - 1) {
                submitDateTime()
            } else viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }
        return view;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_DAY, day)
        outState.putInt(KEY_MONTH, month)
        outState.putInt(KEY_YEAR, year)
        outState.putInt(KEY_HOUR, hour)
        outState.putInt(KEY_MINUTE, minute)

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        day = savedInstanceState.getInt(KEY_DAY)
        month = savedInstanceState.getInt(KEY_MONTH)
        year = savedInstanceState.getInt(KEY_YEAR)
        hour = savedInstanceState.getInt(KEY_HOUR)
        minute = savedInstanceState.getInt(KEY_MINUTE)
    }

    private inline fun setupButtons(viewPagerPosition: Int) = when (viewPager.currentItem) {
        0 -> {
            nextButton.setText(string.datetimepicker_next)
            nextButton.visibility = View.VISIBLE
            backButton.visibility = View.INVISIBLE
        }
        1 -> {
            nextButton.setText(string.datetimepicker_finish)
            nextButton.visibility = View.VISIBLE
            backButton.visibility = View.VISIBLE
        }
        else -> throw IllegalArgumentException("Page unsupported")
    }

    private inline fun submitDateTime() {

    }

    override fun onDatePicked(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    override fun onTimePicked(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
    }
}