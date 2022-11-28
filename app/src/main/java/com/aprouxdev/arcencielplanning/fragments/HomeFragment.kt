package com.aprouxdev.arcencielplanning.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.aprouxdev.arcencielplanning.adapters.AlertAdapter
import com.aprouxdev.arcencielplanning.adapters.AlertCallback
import com.aprouxdev.arcencielplanning.databinding.FragmentHomeBinding
import com.aprouxdev.arcencielplanning.models.Alert
import com.aprouxdev.arcencielplanning.models.AlertType


class HomeFragment : Fragment(), AlertCallback {

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupInformationRecyclerView() {
        val alertList = listOf(
            Alert(id = 1, type = AlertType.General, title = "Information Noël", body = "La braderie de noël aura lieu à l'arande le 9 décambre prochain ! \n N'oubliez pas de prévenir vos proches pour nous donner un coup de main le vendredi soir.", endDate = "01012023"),
        Alert(id = 2, type = AlertType.Urgency, title = "Attention urgent", body = "Il manque trois bénévoles pour le samedi 12 Novembre. \nCliquez ici pour vous inscrire", endDate = "01012023"),
        Alert(id = 3, type = AlertType.Team, title = "Tri des jouets", body = "Salut l'équipe, pour information des cartons de puzzle sont à vérifier dans le local", endDate = "01012023"),
        Alert(id = 4, type = AlertType.Shop, title = "Vacances de la Toussaint", body = "Le magasin sera fermé les samedis 23 Octobre et 2 Novembre", endDate = "01012023"),
        Alert(id = 5, type = AlertType.Assiduity, title = "On ne te vois plus !?", body = "On espère que tu vas bien ? \n Petit rappel : il est demandé de faire au moins un samedi par mois en tant que bénévole au magasin", endDate = "01012023"))

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

}
