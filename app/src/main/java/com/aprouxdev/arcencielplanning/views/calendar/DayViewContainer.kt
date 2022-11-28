package com.aprouxdev.arcencielplanning.views.calendar

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.databinding.ViewCalendarDayBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

interface OnCalendarCallback {
    fun onDaySelected(date: LocalDate)
}

class DayViewContainer(view: View, listener: OnCalendarCallback) : ViewContainer(view) {

    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = ViewCalendarDayBinding.bind(view)

    val dayTextView: AppCompatTextView = binding.calendarDayDayText
    val dayNumberView: AppCompatTextView = binding.calendarDayDayNumber
    val dotView: View = binding.calendarDayDot
    val backgroundView: LinearLayoutCompat = binding.calendarDayBackground

    init {
        view.setOnClickListener {
            listener.onDaySelected(day.date)
        }
    }

    fun setState(
        context: Context?,
        isSelected: Boolean = false,
        hasItem: Boolean = false,
        isToday: Boolean = false,
    ) {
        context?.let {
            val texColorRes = when {
                isSelected -> R.color.white
                else -> R.color.greys_700
            }
            backgroundView.isSelected = isSelected
            backgroundView.isEnabled = isToday
            dayTextView.apply {
                setTextColor(ContextCompat.getColor(context, texColorRes))
            }
            dayNumberView.apply {
                setTextColor(ContextCompat.getColor(context, texColorRes))
            }

            dotView.apply {
                isInvisible = !hasItem
            }
        }
    }
}
