package com.aprouxdev.arcencielplanning.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.io.Serializable
import java.util.*
import kotlin.math.min

interface TimePickerDialogCallback : Serializable {
    fun onTimeSelected(hour: Int, minute: Int)
}

class TimePickerDialogFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    companion object {
        const val TAG = "TimePickerDialogFragment"
        private const val ARG_CALLBACK = "ARG_CALLBACK"
        private const val ARG_HOUR = "ARG_HOUR"
        private const val ARG_MINUTE = "ARG_MINUTE"
        fun newInstance(hour: Int?, minute: Int?, listener: TimePickerDialogCallback): TimePickerDialogFragment{
            val args = Bundle()
            args.putSerializable(ARG_CALLBACK, listener)
            hour?.let { args.putInt(ARG_HOUR, it) }
            minute?.let { args.putInt(ARG_MINUTE, it) }
            val fragment = TimePickerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mListener: TimePickerDialogCallback? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListener = arguments?.getSerializable(ARG_CALLBACK) as TimePickerDialogCallback?
        mHour = arguments?.getInt(ARG_HOUR)
        mMinute = arguments?.getInt(ARG_MINUTE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val now = Calendar.getInstance(Locale.getDefault())
        val hour = mHour ?: now.get(Calendar.HOUR_OF_DAY)
        val minute = mMinute ?: now.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }


    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        mListener?.onTimeSelected(hour = p1, minute = p2)
    }
}