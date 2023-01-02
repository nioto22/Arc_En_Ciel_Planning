package com.aprouxdev.arcencielplanning.data.services

import com.aprouxdev.arcencielplanning.data.models.User
import comaprouxdevarcencielplanning.UserDb


fun UserDb.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        isAdmin = this.isAdmin ?: false,
        imageUrl = this.imageUrl,
        teams = teams?.split(',')?.mapNotNull { it.toIntOrNull() }
    )
}

fun User.toUserDb(): UserDb {
    return UserDb(
        id= this.id,
        name = this.name,
        isAdmin = this.isAdmin,
        imageUrl= this.imageUrl,
        teams= teams?.joinToString(",")
    )
}