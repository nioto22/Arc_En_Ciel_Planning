package com.aprouxdev.arcencielplanning.views.calendar

import android.view.View
import android.widget.TextView
import com.aprouxdev.arcencielplanning.databinding.CalendarDayLegendBinding
import com.kizitonwose.calendar.view.ViewContainer


class WeekViewContainer (view: View) : ViewContainer(view) {
    private val binding = CalendarDayLegendBinding.bind(view)
    val legendLayout = binding.root

    init {
        binding.monday.text = "Lun"
        binding.tuesday.text = "Mar"
        binding.wednesday.text = "Mer"
        binding.thursday.text = "Jeu"
        binding.friday.text = "Ven"
        binding.saturday.text = "Sam"
        binding.sunday.text = "Dim"
    }
}
