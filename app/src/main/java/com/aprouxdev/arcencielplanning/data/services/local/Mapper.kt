package com.aprouxdev.arcencielplanning.data.services.local

import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.models.User
import comaprouxdevarcencielplanning.AlertDb
import comaprouxdevarcencielplanning.EventDb
import comaprouxdevarcencielplanning.UserDb


fun UserDb.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        isAdmin = this.isAdmin ?: false,
        imageUrl = this.imageUrl,
        teams = teams
    )
}

fun User.toUserDb(): UserDb {
    return UserDb(
        id= this.id,
        name = this.name,
        isAdmin = this.isAdmin,
        imageUrl= this.imageUrl,
        teams= teams
    )
}

fun Event.toEventDb(): EventDb {
    return EventDb(
        id= this.id,
        date = this.date.toString(),
        time= this.time,
        team= this.team.ordinal.toLong(),
        users= this.users.joinToString(","),
        title = this.title,
        description= this.description,
        comments = this.comments.joinToString(",")
    )
}

fun Alert.toAlertDb(): AlertDb {
    return AlertDb(
        id= this.id,
        body= this.body,
        endDate= this.endDate.toString(),
        title= this.title,
        type = this.type
    )
}
