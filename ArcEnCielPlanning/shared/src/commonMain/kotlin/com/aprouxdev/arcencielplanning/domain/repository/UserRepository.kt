package com.aprouxdev.arcencielplanning.domain.repository

import com.aprouxdev.arcencielplanning.domain.enums.Teams
import com.aprouxdev.arcencielplanning.domain.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for User operations
 */
interface UserRepository {

    /**
     * Get all users as a Flow
     */
    fun getAllUsers(): Flow<List<User>>

    /**
     * Get a single user by ID
     */
    suspend fun getUserById(id: String): User?

    /**
     * Get admin users
     */
    fun getAdminUsers(): Flow<List<User>>

    /**
     * Get users by team
     */
    fun getUsersByTeam(team: Teams): Flow<List<User>>

    /**
     * Save or update a user
     */
    suspend fun saveUser(user: User): Result<Unit>

    /**
     * Delete a user by ID
     */
    suspend fun deleteUser(id: String): Result<Unit>

    /**
     * Sync local users with remote server
     */
    suspend fun syncUsers(): Result<Unit>

    /**
     * Get current logged-in user
     */
    suspend fun getCurrentUser(): User?
}
