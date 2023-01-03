package com.aprouxdev.arcencielplanning.modals

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.databinding.ModalNewEventBinding
import com.aprouxdev.arcencielplanning.extensions.formattedToString
import com.aprouxdev.arcencielplanning.views.TeamButtonCallback
import com.aprouxdev.arcencielplanning.views.TeamsButton
import com.aprouxdev.arcencielplanning.views.TimePickerDialogCallback
import com.aprouxdev.arcencielplanning.views.TimePickerDialogFragment
import com.aprouxdev.arcencielplanning.views.dialogfragments.*
import com.aprouxdev.arcencielplanning.views.dialogfragments.Period.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Float.max
import java.lang.Float.min
import java.util.*


class NewEventModal : BottomSheetDialogFragment(), TeamButtonCallback, TimePickerDialogCallback,
    DatePickerDialogCallback, EventFrequencyCallback {

    companion object {
        const val TAG = "NewEventModal"
        private const val ARG_DATE = "ARG_DATE"
        fun newInstance(date: Date? = null): NewEventModal {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = NewEventModal()
            fragment.arguments = args
            return fragment
        }
    }


    private var _binding: ModalNewEventBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mContext: Context
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var mSelectedTeam: Teams? = null
    private lateinit var mSelectedDate: Date
    private var mSelectedHour: Int = 8
    private var mSelectedMinute: Int = 0
    private var mComment: String? = null
    private var mOtherTeamDescription: String? = null

    private var mFrequencyNumber: Int = 1
    private var mFrequencyPeriod: Period = DAY
    private var mFrequency: Int = 1


    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE METHODS
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSelectedDate = arguments?.getSerializable(ARG_DATE) as Date? ?: Calendar.getInstance().time
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ModalNewEventBinding.inflate(inflater, container, false)

        setupDataObservers()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialogInterface: DialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                mBehavior = BottomSheetBehavior.from(it)
                mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                activity?.resources?.displayMetrics?.heightPixels?.let {
                    mBehavior.maxHeight = (it * 0.95).toInt()
                    //mBehavior.peekHeight = (it * 0.95).toInt()
                }
                mBehavior.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            mBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }
                })
            }
        }
        return bottomSheetDialog
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUiViews()
        setupListeners()
    }

    //region DATA OBSERVER
    private fun setupDataObservers() {

    }
    //endregion

    //region UI VIEWS
    private fun setupUiViews() {
        setupTeamsButton()
        setupDateTimePicker()
    }

    private fun setupTeamsButton() {
        binding.newEventTeamContainer.removeAllViews()
        val teams = Teams.values()
        teams.forEach { team ->
            context?.let { context ->
                val isSelected = team.name == mSelectedTeam?.name
                val teamButton = TeamsButton(
                    team,
                    listener = this,
                    isTeamSelected = isSelected,
                    context = context
                )
                binding.newEventTeamContainer.addView(teamButton)
            }
        }
        mOtherTeamDescription = null
        binding.newEventOtherTeamEditText.apply {
            text = mOtherTeamDescription
            isVisible = mSelectedTeam == Teams.Other
        }
    }

    private fun setupDateTimePicker() {
        val selectedTimes = getTimesPicker(mSelectedHour, mSelectedMinute)
        val selectedDates = getDatesPicker(mSelectedDate)
        with(binding) {
            datePickerBeforeTime.text = selectedTimes[0]
            datePickerTime.text = selectedTimes[1]
            datePickerAfterTime.text = selectedTimes[2]

            datePickerBeforeDate.text = selectedDates[0]
            datePickerDate.text = selectedDates[1]
            datePickerAfterDate.text = selectedDates[2]
        }
    }

    private fun getTimesPicker(hour: Int, minute: Int): List<String> {
        val selectedTime =
            "${hour.formattedTwoNumbers()}:${minute.formattedTwoNumbers()}"
        val previousTime = when {
            minute < 15 -> {
                val newHour = if (hour > 0) hour - 1 else 23
                val newMinute = 60 + (minute - 15)
                "${newHour.formattedTwoNumbers()}:${newMinute.formattedTwoNumbers()}"
            }
            else -> "${hour.formattedTwoNumbers()}:${(minute - 15).formattedTwoNumbers()}"
        }
        val nextTime = when {
            minute > 45 -> {
                val newHour = if (hour < 23) hour + 1 else 0
                val newMinute = minute - 60 + 15
                "${newHour.formattedTwoNumbers()}:${newMinute.formattedTwoNumbers()}"
            }
            else -> "${hour.formattedTwoNumbers()}:${(minute + 15).formattedTwoNumbers()}"
        }
        return listOf(previousTime, selectedTime, nextTime)
    }

    private fun getDatesPicker(date: Date): List<String> {
        val selectedDate = Calendar.getInstance().apply {
            time = date
        }
        val previousDate = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, -1)
        }
        val nextDate = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, 1)
        }
        return listOf(previousDate.formattedToString("EEE d MMM yyyy"), selectedDate.formattedToString("EEE d MMM yyyy"), nextDate.formattedToString("EEE d MMM yyyy"))
    }

    //endregion

    //region UI LISTENERS
    private fun setupListeners() {
        with(binding) {
            /*
             * Scroll listener
             */
            newEventScrollView.viewTreeObserver.addOnScrollChangedListener {
                val scrollY = newEventScrollView.scrollY
                val scrollYOffset = min(max(0.001f, (scrollY - 44).toFloat()), 100f)
                newEventTitleCollapsed.alpha = scrollYOffset / 100
                newEventToolbarSeparator.alpha = scrollYOffset / 100
            }

            /*
             * CLOSE BUTTON
             */
            newEventCloseButton.setOnClickListener {
                dismiss()
            }
            /*
            * COMMENT EDIT TEXT
             */
            newEventCommentEditText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(editText: Editable?) {
                    mComment = editText.toString()
                }
            })
            /*
            * OTHER TEAM EDIT TEXT
             */
            newEventOtherTeamEditText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(editText: Editable?) {
                    mOtherTeamDescription = editText.toString()
                }
            })
            /*
            * DATE PICKER
             */
            newEventDatePickerButton.setOnClickListener {
                openDatePickerDialog()
            }
            /*
            * TIME PICKER
             */
            newEventTimePickerButton.setOnClickListener {
                openTimePickerDialog()
            }
            /*
            * FREQUENCY
             */
            newEventFrequencyButton.setOnClickListener {
                openFrequencyPickerDialog()
            }
        }
    }

    private fun openFrequencyPickerDialog() {
        val frequencyArray = resources.getStringArray(R.array.frequency_frequency)
        val frequencyPosition = frequencyArray.indexOfFirst { it == mFrequency.toString() }
        val frequencyDialog = EventFrequencyDialogFragment.newInstance(
            numberOf = mFrequencyNumber,
            period = mFrequencyPeriod,
            frequency = frequencyPosition + 1,
            listener = this)
        frequencyDialog.show(childFragmentManager, EventFrequencyDialogFragment.TAG)
    }

    private fun openTimePickerDialog() {
        val timePickerDialog = TimePickerDialogFragment.newInstance(hour= mSelectedHour, minute= mSelectedMinute, listener = this)
        timePickerDialog.show(childFragmentManager, TimePickerDialogFragment.TAG)
    }

    private fun openDatePickerDialog() {
        val cal = Calendar.getInstance()
        cal.time = mSelectedDate
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialogFragment.newInstance(year, month, day, listener = this)
        datePickerDialog.show(childFragmentManager, DatePickerDialogFragment.TAG)
    }

    override fun onTeamButtonClicked(team: Teams) {
        mSelectedTeam = team
        setupTeamsButton()
    }

    override fun onTimeSelected(hour: Int, minute: Int) {
        mSelectedHour = hour
        mSelectedMinute = minute
        setupDateTimePicker()
    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        mSelectedDate = cal.time
        setupDateTimePicker()
    }

    override fun onEventFrequencySelected(
        numberOf: Int,
        period: Period,
        frequency: Int
    ) {
        mFrequencyNumber = numberOf
        mFrequencyPeriod = period
        mFrequency = frequency
        val prefix = period.getPrefix()
        val number = if (numberOf == 1) "" else numberOf.toString()
        val frequencyText = "$prefix$number ${period.value} $frequency fois"
        binding.newEventFrequencyButton.text = frequencyText
    }

    override fun cancelFrequency() {
        mFrequencyNumber = 1
        mFrequencyPeriod = DAY
        mFrequency = 1
        binding.newEventFrequencyButton.text = "Aucune"
    }


    //endregion

}



fun Int.formattedTwoNumbers(): String {
    return if (this < 10) "0$this" else this.toString()
}