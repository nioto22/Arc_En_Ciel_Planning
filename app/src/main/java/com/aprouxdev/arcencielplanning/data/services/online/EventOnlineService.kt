package com.aprouxdev.arcencielplanning.data.services.online

import com.aprouxdev.arcencielplanning.data.models.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface OnlineQueryCallback {
    fun onSuccessListener()
    fun onFailureListener(message: String?)
}

internal class EventOnlineService {
    companion object {
        val instance = EventOnlineService()
        val eventDatabase = Firebase.firestore.collection("event")
    }

    fun addEvent(event: Event, listener: OnlineQueryCallback) {
        val eventData = getMapData(event)
        eventDatabase
            .add(eventData)
            .addOnSuccessListener {
                listener.onSuccessListener()
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }

    fun updateEvent(eventId: String, event: Event, listener: OnlineQueryCallback) {
        val eventUpdated = getMapData(event)
        eventDatabase
            .document(eventId).update(eventUpdated)
            .addOnSuccessListener {
                listener.onSuccessListener()
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }

    fun deleteEvent(eventId: String, listener: OnlineQueryCallback) {
        eventDatabase
            .document(eventId)
            .delete()
            .addOnSuccessListener {
                listener.onSuccessListener()
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }

    fun addMultipleEvent(events: List<Event>, listener: OnlineQueryCallback) {
        Firebase.firestore.runBatch { batch ->
            events.forEach { event ->
                val eventRef = eventDatabase.document(event.id)
                batch.set(eventRef, getMapData(event))
            }
        }.addOnCompleteListener {
            listener.onSuccessListener()
        }.addOnFailureListener {
                listener.onFailureListener(it.message)
            }
    }


    private fun getMapData(event: Event): Map<String, Any?> {
        return hashMapOf(
            "date" to event.date,
            "time" to event.time,
            "users" to event.users,
            "team" to event.team,
            "title" to event.title,
            "description" to event.description
        )
    }

}