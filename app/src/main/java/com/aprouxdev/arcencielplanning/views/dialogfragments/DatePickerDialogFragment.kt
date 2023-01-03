package com.aprouxdev.arcencielplanning.views.dialogfragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.io.Serializable
import java.util.*

interface DatePickerDialogCallback : Serializable {
    fun onDateSelected(year: Int, month: Int, day: Int)
}

class DatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val TAG = "DatePickerDialogFragment"
        private const val ARG_CALLBACK = "ARG_CALLBACK"
        private const val ARG_YEAR = "ARG_YEAR"
        private const val ARG_MONTH = "ARG_MONTH"
        private const val ARG_DAY = "ARG_DAY"
        fun newInstance(year: Int, month: Int, day: Int, listener: DatePickerDialogCallback): DatePickerDialogFragment {
            val args = Bundle()
            args.putSerializable(ARG_CALLBACK, listener)
            args.putInt(ARG_YEAR, year)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_DAY, day)
            val fragment = DatePickerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mListener: DatePickerDialogCallback? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListener = arguments?.getSerializable(ARG_CALLBACK) as DatePickerDialogCallback?
        mYear = arguments?.getInt(ARG_YEAR)
        mMonth = arguments?.getInt(ARG_MONTH)
        mDay = arguments?.getInt(ARG_DAY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val now = Calendar.getInstance(Locale.getDefault())
        val year = mYear ?: now.get(Calendar.YEAR)
        val month = mMonth ?: now.get(Calendar.MONTH)
        val day = mDay ?: now.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        mListener?.onDateSelected(year, month, day)
    }
}