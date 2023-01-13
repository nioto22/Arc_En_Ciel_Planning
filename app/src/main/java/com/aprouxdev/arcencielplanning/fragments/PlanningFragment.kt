package com.aprouxdev.arcencielplanning.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprouxdev.arcencielplanning.adapters.PlanningEventAdapter
import com.aprouxdev.arcencielplanning.adapters.PlanningEventListener
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.databinding.FragmentPlanningBinding
import com.aprouxdev.arcencielplanning.extensions.getLegendName
import com.aprouxdev.arcencielplanning.extensions.present
import com.aprouxdev.arcencielplanning.modals.EventDetailModal
import com.aprouxdev.arcencielplanning.modals.NewEventModal
import com.aprouxdev.arcencielplanning.viewmodel.PlanningViewModel
import com.aprouxdev.arcencielplanning.views.calendar.DayViewContainer
import com.aprouxdev.arcencielplanning.views.calendar.OnCalendarCallback
import com.aprouxdev.arcencielplanning.views.calendar.WeekViewContainer
import com.aprouxdev.arcencielplanning.views.dialogfragments.DatePickerDialogCallback
import com.aprouxdev.arcencielplanning.views.dialogfragments.DatePickerDialogFragment
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList


class PlanningFragment : Fragment(), OnCalendarCallback, DatePickerDialogCallback,
    PlanningEventListener {

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

    private lateinit var viewModel: PlanningViewModel

    private var mSelectedDate: LocalDate = LocalDate.now()
    set(value) {
        field = value
        binding.planningNoItemPlaceholder.isVisible = !mAllItemsDate.contains(value)
        setupRecyclerView()
    }
    private var mAllItemsDate = ArrayList<LocalDate>()

    private lateinit var mEventAdapter: PlanningEventAdapter


    private val shortMonthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val calendarDateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanningBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PlanningViewModel::class.java
        ]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUiViews()
    }

    override fun onResume() {
        super.onResume()
        setupUiListeners()
        setupDataObservers()
    }

    private fun setupDataObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupUiViews() {
        mSelectedDate = LocalDate.now()
        binding.calendarDate.text =  calendarDateFormatter.format(mSelectedDate).replaceFirstChar { it.uppercaseChar() }
        setupCalendarView()
        setupRecyclerView()
    }



    private fun setupCalendarView() {
        val daysOfWeek = DayOfWeek.values()

        val today = LocalDate.now()
        val endDate = today.plusMonths(12)
        binding.calendarView.setup(today, endDate, daysOfWeek.first())
        binding.calendarView.scrollToDate(mSelectedDate)

        binding.calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view, this@PlanningFragment)
            override fun bind(container: DayViewContainer, data: WeekDay) {
                val context = context ?: return
                container.day = data
                val monthTv = container.binding.calendarDayDayText
                val dayTv = container.binding.calendarDayDayNumber
                val background = container.binding.calendarDayBackground
                background.isSelected = data.date == mSelectedDate
                val newTypeface =
                    if (data.date == today || data.date == mSelectedDate) Typeface.BOLD
                    else Typeface.NORMAL
                monthTv.apply {
                    text =
                        shortMonthFormatter.format(data.date.month)
                            .replaceFirstChar { it.lowercase() }
                    setTypeface(this.typeface, newTypeface)
                }
                dayTv.apply {
                    text = data.date.dayOfMonth.toString()
                    setTypeface(this.typeface, newTypeface)
                }
                container.setState(
                    context = context,
                    isSelected = data.date == mSelectedDate,
                    hasItem = mAllItemsDate.contains(data.date),
                    isToday = data.date == today
                )
            }
        }

        binding.calendarView.weekScrollListener = {
            // In week mode, we show the header a bit differently.
            // We show indices with dates from different months since
            // dates overflow and cells in one index can belong to different
            // months/years.
            val firstDate = it.days.first().date
            val lastDate = it.days.last().date
            val calendarText = if (firstDate.yearMonth == lastDate.yearMonth) {
                calendarDateFormatter.format(firstDate)
                    .replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
            } else {
                val monthText =
                    "${
                        monthTitleFormatter.format(firstDate)
                            .replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
                    } - " +
                            monthTitleFormatter.format(lastDate)
                                .replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
                val yearText = if (firstDate.year == lastDate.year) {
                    firstDate.yearMonth.year.toString()
                } else {
                    "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                }
                "$monthText $yearText"
            }
            binding.calendarDate.text = calendarText
        }

        binding.calendarView.weekHeaderBinder = object : WeekHeaderFooterBinder<WeekViewContainer> {
            override fun create(view: View): WeekViewContainer = WeekViewContainer(view)
            override fun bind(container: WeekViewContainer, data: Week) {
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = data.days.first().date.dayOfYear
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getLegendName()
                        }
                }
            }

        }
    }

    private fun setupRecyclerView() {
        val hasItem = mAllItemsDate.contains(mSelectedDate)
        val selectedItems = viewModel.getItemForDate()
        if (this::mEventAdapter.isInitialized) {
            mEventAdapter.updateData(selectedItems)
        } else {
            with(binding.planningPlanningRecyclerview) {
                layoutManager = LinearLayoutManager(context)
                mEventAdapter = PlanningEventAdapter(context = context,
                data = selectedItems,
                listener = this@PlanningFragment
                )
                adapter = mEventAdapter
            }
        }
    }


    private fun setupUiListeners() {
        with(binding) {
            calendarSelectDateButton.setOnClickListener {
                val datePickerFragment = DatePickerDialogFragment.newInstance(
                    year = mSelectedDate.year,
                    month = mSelectedDate.monthValue - 1,
                    day = mSelectedDate.dayOfMonth,
                    listener = this@PlanningFragment
                )
                datePickerFragment.show(childFragmentManager, DatePickerDialogFragment.TAG)
            }
            planningAddEventButton.setOnClickListener {
                val date = Date(mSelectedDate.year, mSelectedDate.monthValue, mSelectedDate.dayOfMonth)
                val addEventModal = NewEventModal.newInstance(date)
                addEventModal.present(childFragmentManager, NewEventModal.TAG)
            }
        }
    }

    override fun onDaySelected(date: LocalDate) {
        val oldDate = mSelectedDate
        mSelectedDate = date
        binding.calendarView.apply {
            notifyDateChanged(oldDate)
            notifyDateChanged(mSelectedDate)
        }
    }

    /**
     * Date picker callback
     */
    override fun onDateSelected(year: Int, month: Int, day: Int) {
        val oldDate = mSelectedDate
        val newDate = LocalDate.of(year, getMonth(month + 1), day)
        mSelectedDate = newDate
        binding.calendarView.apply {
            notifyDateChanged(oldDate)
            notifyDateChanged(mSelectedDate)
            smoothScrollToDate(mSelectedDate)
        }
    }

    override fun onPlanningEventClicked(event: Event) {
        val eventDetailModal = EventDetailModal.newInstance(event)
        eventDetailModal.present(childFragmentManager, EventDetailModal.TAG)
    }

    private fun getMonth(month: Int): Month {
      return when(month) {
            1 -> Month.JANUARY
            2 -> Month.FEBRUARY
            3 -> Month.MARCH
            4 -> Month.APRIL
            5 -> Month.MAY
            6 -> Month.JUNE
            7 -> Month.JULY
            8 -> Month.AUGUST
            9 -> Month.SEPTEMBER
            10 -> Month.OCTOBER
            11 -> Month.NOVEMBER
            12 -> Month.DECEMBER
            else -> Month.JANUARY
        }
    }



}
