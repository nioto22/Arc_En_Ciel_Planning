package com.aprouxdev.arcencielplanning.data.services

import com.aprouxdev.arcencielplanning.data.models.User
import comaprouxdevarcencielplanning.UserDb


interface UserService {

    suspend fun getUserById(id: String): UserDb?

    suspend fun deleteUserById(id: String)
    suspend fun insertUser(user: User)
}
