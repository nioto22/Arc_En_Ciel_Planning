package com.aprouxdev.arcencielplanning.views.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.databinding.DialogFragmentEventFrequencyBinding
import com.aprouxdev.arcencielplanning.databinding.DialogFragmentProcessStateBinding
import com.aprouxdev.arcencielplanning.viewmodel.ProcessState


class ProcessStateDialogFragment: DialogFragment() {

    companion object {
        const val TAG = "ProcessStateDialogFragment"
        private const val ARG_STATE = "ARG_STATE"
        fun newInstance(initialState: ProcessState): ProcessStateDialogFragment{
            val args = Bundle()
            args.putSerializable(ARG_STATE, initialState)
            val fragment = ProcessStateDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: DialogFragmentProcessStateBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var mState: ProcessState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mState = arguments?.getSerializable(ARG_STATE) as ProcessState
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentProcessStateBinding.inflate(inflater, container, false)

        isCancelable = true
        setupViews()

        return binding.root
    }

    private fun setupViews() {
        with(binding) {
            processStateLoader.isVisible = mState == ProcessState.IsLoading
            val drawableRes = when(mState) {
                is ProcessState.IsSuccessful -> R.drawable.ic_validated
                is ProcessState.IsFailed -> R.drawable.ic_error
                else -> 0
            }
            processStateImage.apply {
                isVisible = drawableRes != 0
                setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
            }

            val dialogText = when(val state = mState) {
                is ProcessState.IsSuccessful -> state.message
                is ProcessState.IsFailed -> state.message
                else -> null
            }
            processStateText.apply {
                isVisible = text != null
                text = dialogText
            }
        }
    }

    fun updateState(state: ProcessState) {
        mState = state
        setupViews()
    }

}