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
import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.databinding.ModalAlertDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AlertDetailModal : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AlertDetailModal"
        const val ARG_ALERT = "ARG_ALERT"
        fun newInstance(alert: Alert): AlertDetailModal {
            val args = Bundle()
            args.putSerializable(ARG_ALERT, alert)
            val fragment = AlertDetailModal()
            fragment.arguments = args
            return fragment
        }
    }


    private var _binding: ModalAlertDetailBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mContentView: View
    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mContext: Context
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var bottomSheetDialog: BottomSheetDialog


    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE METHODS
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ModalAlertDetailBinding.inflate(inflater, container, false)
        mContentView = binding.root

        setupDataObservers()

        return mContentView
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

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
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