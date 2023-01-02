package com.aprouxdev.arcencielplanning.modals

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.databinding.ModalNewEventBinding
import com.aprouxdev.arcencielplanning.extensions.formattedToString
import com.aprouxdev.arcencielplanning.views.TeamButtonCallback
import com.aprouxdev.arcencielplanning.views.TeamsButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Float.max
import java.lang.Float.min
import java.util.*


class NewEventModal : BottomSheetDialogFragment(), TeamButtonCallback {

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

    private var mSelectedTeam: Teams? = Teams.Other
    private lateinit var mSelectedDate: Date
    private var mSelectedHour = 9
    private var mSelectedMinute = 0

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
                activity?.resources?.displayMetrics?.heightPixels?.let {
                    mBehavior.maxHeight = (it * 0.95).toInt()
                    mBehavior.peekHeight = (it * 0.85).toInt()
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


    private fun closeModal() {
        mBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun dismiss() {
        super.dismiss()
        onCloseMethod()
    }


    private fun setupDataObservers() {

    }


    private fun setupUiViews() {
        setupTeamsButton()
        setupDateTimePicker()
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
        return listOf(previousDate.formattedToString("dd MMM yyyy"), selectedDate.formattedToString("dd MMM yyyy"), nextDate.formattedToString("dd MMM yyyy"))
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
    }

    private fun setupListeners() {
        with(binding) {
            /*
             * Scroll listener
             */
            newEventScrollView.viewTreeObserver.addOnScrollChangedListener {
                val scrollY = newEventScrollView.scrollY
                val scrollYOffset = min(max(0.001f, (scrollY - 25).toFloat()), 100f)
                newEventTitleCollapsed.alpha = scrollYOffset / 100
                newEventToolbarSeparator.alpha = scrollYOffset / 100
            }

            /*
             * CLOSE BUTTON
             */
            newEventCloseButton.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun onCloseMethod() {


    }

    override fun onTeamButtonClicked(team: Teams) {
        mSelectedTeam = team
        setupTeamsButton()
    }

}



fun Int.formattedTwoNumbers(): String {
    return if (this < 10) "0$this" else this.toString()
}