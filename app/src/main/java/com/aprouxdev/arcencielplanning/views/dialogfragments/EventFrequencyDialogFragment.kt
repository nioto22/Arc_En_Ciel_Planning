package com.aprouxdev.arcencielplanning.views.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.databinding.DialogFragmentEventFrequencyBinding
import java.io.Serializable

interface EventFrequencyCallback : Serializable {
    fun onEventFrequencySelected(numberOf: Int, period: Period, frequency: Int)
}

class EventFrequencyDialogFragment: DialogFragment(), AdapterView.OnItemSelectedListener {

    companion object {
        const val TAG = "EventFrequencyDialogFragment"
        private const val ARG_CALLBACK = "ARG_CALLBACK"
        private const val ARG_NUMBER = "ARG_NUMBER"
        private const val ARG_PERIOD = "ARG_PERIOD"
        private const val ARG_FREQUENCY = "ARG_FREQUENCY"
        fun newInstance(numberOf: Int, period: Period, frequency: Int, listener: EventFrequencyCallback): EventFrequencyDialogFragment{
            val args = Bundle()
            args.putSerializable(ARG_CALLBACK, listener)
            args.putInt(ARG_NUMBER, numberOf)
            args.putSerializable(ARG_PERIOD, period)
            args.putSerializable(ARG_FREQUENCY, frequency)
            val fragment = EventFrequencyDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: DialogFragmentEventFrequencyBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var mListener: EventFrequencyCallback? = null

    private var mNumber: Int = 1
    private var mPeriod: Period = Period.DAY
    private var mFrequency: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListener = arguments?.getSerializable(ARG_CALLBACK) as EventFrequencyCallback?
        mNumber = arguments?.getInt(ARG_NUMBER) ?: 1
        mPeriod = arguments?.getSerializable(ARG_PERIOD) as Period? ?: Period.DAY
        mFrequency = arguments?.getInt(ARG_FREQUENCY) ?: 1

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentEventFrequencyBinding.inflate(inflater, container, false)

        isCancelable = true
        setupViews()
        setupUiListeners()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViews() {
        setupSpinners()
    }

    private fun setupSpinners() {
        with(binding) {
            setupSpinnerView(spinner= spinnerNumber, arrayData= R.array.frequency_number, defaultValue = mNumber - 1)
            setupSpinnerView(spinner= spinnerPeriod, arrayData= R.array.frequency_period, defaultValue = mPeriod.ordinal)
            setupSpinnerView(spinner= spinnerFrequency, arrayData= R.array.frequency_frequency, defaultValue = mFrequency - 1)
        }
    }

    private fun setupSpinnerView(spinner: Spinner, arrayData: Int, defaultValue: Int) {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                arrayData,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
                spinner.setSelection(defaultValue)
            }
            spinner.onItemSelectedListener = this
        }
    }


    private fun setupUiListeners() {
        with(binding) {
            frequencyCancelButton.setOnClickListener { dismiss() }
            frequencyValidateButton.setOnClickListener {
                mListener?.onEventFrequencySelected(
                    numberOf = mNumber,
                    period = mPeriod,
                    frequency = mFrequency
                )
                dismiss()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        when(parent?.id) {
            R.id.spinner_number -> mNumber = pos + 1
            R.id.spinner_period -> mPeriod = Period.values()[pos]
            R.id.spinner_frequency -> mFrequency = pos + 1
            else -> Unit
        }
        binding.frequencyPrefix.text = mPeriod.getPrefix()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}

enum class Period(val value :String) {
    DAY("jours"), WEEK("semaines"), MONTH("mois"), YEAR("années");

    fun getPrefix(): String {
        return when(this) {
            DAY -> "Tous les "
            WEEK -> "Toutes les "
            MONTH -> "Tous les "
            YEAR -> "Toutes les "
        }
    }
}
