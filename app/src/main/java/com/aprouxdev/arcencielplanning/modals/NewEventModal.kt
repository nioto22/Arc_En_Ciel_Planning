package com.aprouxdev.arcencielplanning.modals

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.databinding.ModalNewEventBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NewEventModal : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NewEventModal"
        fun newInstance(): NewEventModal {
            val args = Bundle()

            val fragment = NewEventModal()
            fragment.arguments = args
            return fragment
        }
    }


    private var _binding: ModalNewEventBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mContentView: View
    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mContext: Context
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val setFullHeight: Boolean = false

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE METHODS
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       _binding = ModalNewEventBinding.inflate(inflater, container, false)
        mContentView = binding.root

        setupDataObservers()

        return mContentView
    }


    override fun onDestroyView() {
        super.onDestroyView()
      //  _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialogInterface: DialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                mBehavior = BottomSheetBehavior.from(it)
                mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
        mFragmentManager = requireActivity().supportFragmentManager
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


    }

    private fun setupListeners() {

    }

    private fun onCloseMethod() {


    }

}