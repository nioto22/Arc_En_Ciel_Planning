package com.aprouxdev.arcencielplanning.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.aprouxdev.arcencielplanning.adapters.*
import com.aprouxdev.arcencielplanning.data.enums.AlertType
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.mock.MockData
import com.aprouxdev.arcencielplanning.databinding.FragmentHomeBinding
import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event


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
    private lateinit var mHomeEventContainerAdapter: HomeEventContainerAdapter

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupInformationRecyclerView() {
        val alertList = listOf(MockData.alert1, MockData.alert2, MockData.alert3, MockData.alert4, MockData.alert5)

         with(binding.homeInformationRecyclerview) {
             layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
             val snapHelper = PagerSnapHelper()
             mAlertAdapter = AlertAdapter(alertList, this@HomeFragment)
             adapter = mAlertAdapter
             snapHelper.attachToRecyclerView(this)
         }
    }

    override fun onAlertClicked(alert: Alert) {
        Toast.makeText(context, "Alert : ${alert.title}", Toast.LENGTH_SHORT).show()
    }




    private fun setupPlanningRecyclerView() {

        val yourEventList = listOf<HomeEventData>(
            HomeEventData("22 Dec. 2022", listOf(MockData.event1, MockData.event2)),
            HomeEventData("01 Jan. 2023", listOf(MockData.event3)),
            HomeEventData("02 Jan. 2023", listOf(MockData.event4, MockData.event5, MockData.event6)),
            HomeEventData("12 Jan. 2023", listOf(MockData.event7)),
        )
        with(binding.homePlanningRecyclerview) {
            layoutManager = LinearLayoutManager(context)
            mHomeEventContainerAdapter = HomeEventContainerAdapter(
                data = yourEventList,
                listener = this@HomeFragment
            )
            adapter = mHomeEventContainerAdapter
        }
    }

    override fun onHomeEventClicked(event: Event) {
        Toast.makeText(context, "Event : ${event.title}", Toast.LENGTH_SHORT).show()
    }

}
