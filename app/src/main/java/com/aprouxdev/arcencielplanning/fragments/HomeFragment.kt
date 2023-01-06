package com.aprouxdev.arcencielplanning.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SnapHelper
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
import java.util.*


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

    private lateinit var mAlertAdapter: AlertAdapter
    private lateinit var mHomeEventContainerAdapter: HomeEventAdapter
    private var mEventList: List<Event> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInformationRecyclerView()
        setupPlanningRecyclerView()
        setupPlanningButtons()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupInformationRecyclerView() {
        val alertList = listOf(
            MockData.alert1,
            MockData.alert2,
            MockData.alert3,
            MockData.alert4,
            MockData.alert5
        )

        if (this::mAlertAdapter.isInitialized) {
            mAlertAdapter.updateData(alertList)
        } else {
            with(binding.homeInformationRecyclerview) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val snapHelper = PagerSnapHelper()
                mAlertAdapter = AlertAdapter(alertList, this@HomeFragment)
                adapter = mAlertAdapter
                snapHelper.attachToRecyclerView(this)
            }
        }
    }


    private fun setupPlanningRecyclerView() {
        //region MOCK DATA
        val now = Calendar.getInstance(Locale.getDefault())
        val date1 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }.time
        val date2 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }.time
        val date3 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 2)
        }.time
        val date4 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 8)
        }.time
        val date5 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 9)
        }.time
        val date6 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 32)
        }.time
        val date7 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 32)
        }.time
        val date8 = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 55)
        }.time
        mEventList = listOf(MockData.event1.apply { date = date1 },
            MockData.event2.apply { date = date2 },
            MockData.event3.apply { date = date3 },
            MockData.event4.apply { date = date4 },
            MockData.event5.apply { date = date5 },
            MockData.event6.apply { date = date6 },
            MockData.event7.apply { date = date7 },
            MockData.event8.apply { date = date8 })
        //endregion

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

}
