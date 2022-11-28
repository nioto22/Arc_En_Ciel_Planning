package com.aprouxdev.arcencielplanning.views

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.NumberPicker
import android.widget.TimePicker


class CustomTimePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TimePicker(context, attrs) {

    private var timePickerInterval = 1

    init {
        setupViews()
    }

    override fun setOnTimeChangedListener(onTimeChangedListener: OnTimeChangedListener?) {

        super.setOnTimeChangedListener(onTimeChangedListener)
    }

    fun updateTimeInterval(interval: Int) {
        this.timePickerInterval = interval
        setupViews()
    }

    private fun setupViews() {
        this.descendantFocusability = FOCUS_BLOCK_DESCENDANTS
        setupTimePickerViews()
    }

    /**
     * Remove all minutes value inside timePickerInterval
     */
    private fun setupTimePickerViews() {
        try {
            val minuteRes = Resources.getSystem().getIdentifier(
                "minute", "id", "android"
            )
            val minutePicker = this.findViewById<NumberPicker>(minuteRes)
            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / timePickerInterval - 1
            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += timePickerInterval
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
