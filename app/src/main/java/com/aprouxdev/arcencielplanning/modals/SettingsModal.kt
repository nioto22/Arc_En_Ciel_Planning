package com.aprouxdev.arcencielplanning.modals

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.enums.getTeamByName
import com.aprouxdev.arcencielplanning.databinding.ModalSettingsBinding
import com.aprouxdev.arcencielplanning.viewmodel.SettingsViewModel
import com.aprouxdev.arcencielplanning.views.TeamButtonCallback
import com.aprouxdev.arcencielplanning.views.TeamsButton
import com.aprouxdev.arcencielplanning.views.TeamsTextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import comaprouxdevarcencielplanning.UserDb
import kotlinx.coroutines.launch


class SettingsModal : BottomSheetDialogFragment(), TeamButtonCallback {

    companion object {
        const val TAG = "SettingsModal"
        fun newInstance(): SettingsModal {
            val args = Bundle()

            val fragment = SettingsModal()
            fragment.arguments = args
            return fragment
        }
    }


    private var _binding: ModalSettingsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModel: SettingsViewModel

    private lateinit var mContentView: View
    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mContext: Context
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val setFullHeight: Boolean = true

    private var mUser: UserDb? = null
    private var mTeamSelected: Teams? = null

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE METHODS
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ModalSettingsBinding.inflate(inflater, container, false)
        mContentView = binding.root
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
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
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                mBehavior = BottomSheetBehavior.from(it)
                if (setFullHeight) setupFullHeight(it)
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

    override fun dismiss() {
        super.dismiss()
        onCloseMethod()
    }


    private fun setupDataObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect {
                mUser = it.firstOrNull()
                setupUiViews()
            }
        }
    }


    private fun setupUiViews() {
        binding.settingsUserName.text = mUser?.name
        setupAvatar()
        setupTeams()
        setupAdminView()

    }

    private fun setupAdminView() {
        binding.settingsAdminContainer.isVisible = mUser?.isAdmin == true
    }

    private fun setupAvatar() {

    }

    private fun setupTeams() {
        val teamTitle = binding.settingsUserTeamsTitle
        binding.settingsUserTeamsContainer.apply {
            removeAllViews()
            addView(teamTitle)
            mUser?.teams?.forEach {
                val team = it.getTeamByName()
                val teamsItem = TeamsTextView(this.context).apply { setName(team.getName()) }
                addView(teamsItem)
                val teamsSeparator = TeamsTextView(this.context).apply { setName("/") }
                addView(teamsSeparator)
            }
        }


}

    private fun setupListeners() {
        binding.settingsAddAlertButton.setOnClickListener {

        }
    }

    private fun onCloseMethod() {


    }

    override fun onTeamButtonClicked(team: Teams) {
        TODO("Not yet implemented")
    }

}