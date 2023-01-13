package com.aprouxdev.arcencielplanning.modals

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.databinding.ModalEventDetailBinding
import com.aprouxdev.arcencielplanning.extensions.formattedToString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EventDetailModal : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EventDetailModal"
        const val ARG_EVENT = "ARG_EVENT"
        fun newInstance(event: Event): EventDetailModal {
            val args = Bundle()
            args.putSerializable(ARG_EVENT, event)

            val fragment = EventDetailModal()
            fragment.arguments = args
            return fragment
        }
    }


    private var _binding: ModalEventDetailBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mContext: Context
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var mEvent: Event? = null


    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE METHODS
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mEvent = arguments?.getSerializable(ARG_EVENT) as Event?
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ModalEventDetailBinding.inflate(inflater, container, false)

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
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
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


    private fun setupDataObservers() {

    }


    private fun setupUiViews() {
        with(binding) {
            mEvent?.let {
                val teamText = if (it.getTeamByName() == Teams.Other) it.title else it.getTeamByName().getName()
                eventDetailTeam.text = teamText
                eventDetailTeamCollapsed.text = teamText

                eventDetailTitle.apply {
                    isVisible = it.getTeamByName() != Teams.Other && it.title.isNullOrEmpty()
                    text = it.title
                }

                eventDetailDate.apply {
                    isVisible = it.date != null
                    text = it.date?.formattedToString("EEEE d MMMM yyyy")
                }

                eventDetailTime.apply {
                    isVisible = it.time != null
                    text = it.time
                }
            }
        }

    }

    private fun setupListeners() {

    }
}