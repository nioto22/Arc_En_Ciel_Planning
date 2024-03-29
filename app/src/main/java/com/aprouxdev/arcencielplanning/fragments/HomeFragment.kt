package com.aprouxdev.arcencielplanning.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.aprouxdev.arcencielplanning.adapters.AlertAdapter
import com.aprouxdev.arcencielplanning.adapters.AlertCallback
import com.aprouxdev.arcencielplanning.adapters.HomeEventAdapter
import com.aprouxdev.arcencielplanning.adapters.HomeEventListener
import com.aprouxdev.arcencielplanning.data.mock.MockData
import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.databinding.FragmentHomeBinding
import com.aprouxdev.arcencielplanning.extensions.getCurrentItem
import com.aprouxdev.arcencielplanning.extensions.hasNext
import com.aprouxdev.arcencielplanning.extensions.hasPreview
import com.aprouxdev.arcencielplanning.extensions.present
import com.aprouxdev.arcencielplanning.modals.EventDetailModal
import com.aprouxdev.arcencielplanning.modals.SettingsModal
import com.aprouxdev.arcencielplanning.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), AlertCallback, HomeEventListener {

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModel: HomeViewModel

    private lateinit var mAlertAdapter: AlertAdapter
    private var mAlertList: List<Alert> = emptyList()
    private lateinit var mHomeEventContainerAdapter: HomeEventAdapter
    private var mEventList: List<Event> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInformationRecyclerView()
        setupEventRecyclerView()
        setupPlanningButtons()
    }

    override fun onResume() {
        super.onResume()
        setupDataObservers()
        setupUiListeners()
    }

    private fun setupDataObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.alertList.collect {
                mAlertList = it
                setupInformationRecyclerView()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventList.collect {
                //mEventList = it
                //TODO REMOVE AFTER TESTS
                //setupEventRecyclerView()
            }
        }

    }

    private fun setupUiListeners() {
        with(binding) {
            homeUserAvatar.setOnClickListener {
                showSettings()
            }
            homeUserAvatarText.setOnClickListener {
                showSettings()
            }
        }
    }

    private fun showSettings() {
        val settingsModal = SettingsModal.newInstance()
        settingsModal.present(childFragmentManager, SettingsModal.TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupInformationRecyclerView() {
        // TODO REPLACE AFTER TEST
        mAlertList = listOf(MockData.alert1, MockData.alert2, MockData.alert3, MockData.alert4, MockData.alert5)
        binding.homeInformationRecyclerview.isVisible = mAlertList.isNotEmpty()
        binding.homeInformationPlaceholder.isVisible = mAlertList.isEmpty()
        if (this::mAlertAdapter.isInitialized) {
            mAlertAdapter.updateData(mAlertList)
        } else {
            with(binding.homeInformationRecyclerview) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val snapHelper = PagerSnapHelper()
                mAlertAdapter = AlertAdapter(mAlertList, this@HomeFragment)
                adapter = mAlertAdapter
                snapHelper.attachToRecyclerView(this)
            }
        }
    }


    private fun setupEventRecyclerView() {
        mEventList = listOf(MockData.event1, MockData.event2, MockData.event3, MockData.event4)
        binding.homeEventRecyclerviewContainer.isVisible = mEventList.isNotEmpty()
        binding.homeEventPlaceholder.isVisible = mEventList.isEmpty()
        if (this::mHomeEventContainerAdapter.isInitialized) {
            mHomeEventContainerAdapter.updateData(mEventList)
        } else {
            with(binding.homePlanningRecyclerview) {
                binding.homeEventsLeftButton.isVisible = false
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val planningSnapHelper = PagerSnapHelper()
                mHomeEventContainerAdapter = HomeEventAdapter(
                    context = context,
                    data = mEventList,
                    listener = this@HomeFragment
                )
                adapter = mHomeEventContainerAdapter
                planningSnapHelper.attachToRecyclerView(this)
                this.addOnScrollListener(object : OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            SCROLL_STATE_IDLE -> {
                                binding.homeEventsLeftButton.isVisible = recyclerView.hasPreview()
                                binding.homeEventsRightButton.isVisible =
                                    recyclerView.hasNext(mEventList.size)
                            }
                            SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING -> {
                                with(binding) {
                                    homeEventsLeftButton.isVisible = false
                                    homeEventsRightButton.isVisible = false
                                }
                            }
                        }
                    }
                })

            }
        }
    }


    private fun setupPlanningButtons() {
        with(binding) {
            homeEventsLeftButton.setOnClickListener {
                val recyclerView = homePlanningRecyclerview
                if (recyclerView.hasPreview()) {
                    val position = recyclerView.getCurrentItem()
                    recyclerView.smoothScrollToPosition(position - 1)
                }
            }
            homeEventsRightButton.setOnClickListener {
                val recyclerView = homePlanningRecyclerview
                if (recyclerView.hasNext(mEventList.size)) {
                    val position = recyclerView.getCurrentItem()
                    recyclerView.smoothScrollToPosition(position + 1)
                }
            }
        }
    }


    override fun onAlertClicked(alert: Alert) {
        Toast.makeText(context, "Alert : ${alert.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onHomeEventClicked(event: Event) {
        val eventDetailModal = EventDetailModal.newInstance(event)
        eventDetailModal.present(childFragmentManager, EventDetailModal.TAG)
    }

    fun updateAlerts() {
        viewModel.getAllAlert()
    }

    fun updateEventData() {
        viewModel.getAllUserEvents()
    }

}
