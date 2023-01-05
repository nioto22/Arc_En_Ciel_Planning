package com.aprouxdev.arcencielplanning.fragments

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.aprouxdev.arcencielplanning.databinding.FragmentPlanningBinding
import com.aprouxdev.arcencielplanning.databinding.ViewCalendarDayBinding
import com.aprouxdev.arcencielplanning.views.calendar.DayViewContainer
import com.aprouxdev.arcencielplanning.views.calendar.OnCalendarCallback
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList


class PlanningFragment : Fragment(), OnCalendarCallback {

    companion object {
        const val TAG = "PlanningFragment"
        fun newInstance(): PlanningFragment {
            val args = Bundle()

            val fragment = PlanningFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentPlanningBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var calendarView: CalendarView
    private var mSelectedDate : LocalDate = LocalDate.now()
    private val mAllItemsDate = ArrayList<LocalDate>()

    private var selectedDate = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanningBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiViews()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupUiViews() {
        setupCalendarView2()
    }

    private fun setupCalendarView2() {
        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        binding.calendarView.apply {
            val dayWidth = dm.widthPixels / 5
            val dayHeight = (dayWidth * 1.25).toInt()
            val d = DaySize.Rectangle
            //daySize = Size(dayWidth, dayHeight)
        }

        binding.calendarView.dayBinder = object: MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view, this@PlanningFragment)
            override fun bind(container: DayViewContainer, data: CalendarDay) = container.setState(context, data.date == mSelectedDate)
        }

        val currentMonth = YearMonth.now()
        // Value for firstDayOfWeek does not matter since inDates and outDates are not generated.
        binding.calendarView.setup(currentMonth, currentMonth.plusMonths(3), DayOfWeek.values().random())
        binding.calendarView.scrollToDate(LocalDate.now())
    }

   /* private fun setupCalendarView() {
        calendarView = binding.calendarView

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            val today = LocalDate.now()
            override fun bind(container: DayViewContainer, data: WeekDay) {
                val dayNumber = data.date.dayOfMonth
                val dayText = data.date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
                container.dayTextView.text = dayText
                container.dayNumberView.text = dayNumber.toString()
                container.setState(
                    context = context,
                    isSelected = data.date == mSelectedDate,
                    isToday = data.date == today,
                    hasItem = mAllItemsDate.contains(data.date)
                )
            }

            override fun create(view: View) = DayViewContainer(view, object : OnCalendarCallback{
                override fun onDaySelected(date: LocalDate) {
                    mSelectedDate = date
                }

            })

        }

        // SETUP
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(4)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(12)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        val currentWeekDay = WeekDay(LocalDate.now(), WeekDayPosition.InDate)
        calendarView.setup(startDate = startMonth.atStartOfMonth(), endDate = endMonth.atEndOfMonth(), firstDayOfWeek= firstDayOfWeek)
        calendarView.scrollToDay(currentWeekDay)
        /*val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()

        endMonth = currentMonth.plusMonths(12)
        calendarView.apply {
            doOnPreDraw {
                val dayWidth = calendarView.width / 7
                val dayHeight = (dayWidth * .666).toInt()
                daySize = Size(dayWidth, dayHeight)
            }
            setup(
                startMonth = currentMonth,
                endMonth = currentMonth.plusMonths(12),
                firstDayOfWeek = daysOfWeek.first()
            )
        }

        // Setup calendar toolbar
        calendarView.findFirstVisibleMonth()?.let {
            mCurrentCalendarMonth = it
        }
        if (this::mCurrentCalendarMonth.isInitialized) {
            setupCalendarToolbar(mCurrentCalendarMonth)
        }

        // Setup Day binder
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer =
                DayViewContainer(view = view, listener = this@MainCalendarFragment)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val dayTextView = container.binding.calendarDayTv

                dayTextView.text = day.date.dayOfMonth.toString()
                val dayHorizontalMargin = if (day.date.dayOfMonth > 9) 8 else 16
                dayTextView.margin(
                    left = dayHorizontalMargin.toFloat(),
                    right = dayHorizontalMargin.toFloat()
                )

                if (day.owner == DayOwner.THIS_MONTH) {
                    container.binding.root.isVisible = true
                    val isDisable = day.date < today
                    val hasItem = events.contains(day.date)
                    val isToday = day.date == today
                    val isSelectedDate = day.date == selectedDate
                    container.setState(
                        context,
                        isSelected = isSelectedDate,
                        isToday = isToday,
                        hasItem = hasItem,
                        isDisabled = isDisable
                    )
                } else {
                    container.binding.root.isInvisible = true
                }
            }
        }

        // Scroll listener
        calendarView.monthScrollListener = { calendarMonth ->
            mCurrentCalendarMonth = calendarMonth
            setupCalendarToolbar(calendarMonth)

            // Selected date on Scroll
            // if Month view -> first date of month
            // first date of month = max(firstDate, today)
            if (isMonthView()) {
                val firstDate =
                    if (calendarMonth.yearMonth.atDay(1) < today) today else calendarMonth.yearMonth.atDay(
                        1
                    )
                selectDate(firstDate)
            } else {
                val selectedWeekDayValue = selectedWeekDay ?: 0
                val selectedDate =
                    calendarMonth.weekDays.map { it.firstOrNull { day -> day.date.dayOfWeek.value == selectedWeekDayValue } }
                        .firstOrNull()?.date
                        ?: calendarMonth.weekDays.first().first().date
                selectDate(selectedDate)
            }
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault())
                        }
                }
            }
        }

         */
    }

    */

    override fun onDaySelected(date: LocalDate) {
        //TODO("Not yet implemented")
    }

}
