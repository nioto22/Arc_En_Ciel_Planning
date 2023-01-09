package com.aprouxdev.arcencielplanning.data.services.online

import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.services.local.toAlertDb
import com.aprouxdev.arcencielplanning.data.services.local.toEventDb
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import comaprouxdevarcencielplanning.AlertDb
import comaprouxdevarcencielplanning.EventDb


fun QueryDocumentSnapshot.toEvent(): EventDb? {
    return try {
        val event: Event = this.toObject()
        event.toEventDb()
    } catch (e: Exception) {
        null
    }
}

fun QueryDocumentSnapshot.toAlert(): AlertDb? {
    return try {
        val alert: Alert = this.toObject()
        alert.toAlertDb()
    } catch (e: Exception) {
        null
    }

}