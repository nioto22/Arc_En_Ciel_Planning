package com.aprouxdev.arcencielplanning.data.services.online

import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


internal class AlertOnlineService {

    companion object {
        val instance = AlertOnlineService()
        val alertDatabase = Firebase.firestore.collection("alert")
    }

    fun addAlert(alert: Alert, listener: OnlineQueryCallback) {
        val alertData = getMapData(alert)
        alertDatabase
            .add(alertData)
            .addOnSuccessListener {
                listener.onSuccessListener()
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }

    fun updateAlert(alertId: String, alert: Alert, listener: OnlineQueryCallback) {
        val alertUpdated = getMapData(alert)
        alertDatabase
            .document(alertId).update(alertUpdated)
            .addOnSuccessListener {
                listener.onSuccessListener()
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }

    fun deleteAlert(alertId: String, listener: OnlineQueryCallback) {
        alertDatabase
            .document(alertId)
            .delete()
            .addOnSuccessListener {
                listener.onSuccessListener()
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }


    private fun getMapData(alert: Alert): Map<String, Any?> {
        return hashMapOf(
            "type" to alert.type,
            "title" to alert.title,
            "body" to alert.body,
            "endDate" to alert.endDate
        )
    }

}
