package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.enums.Teams
import com.aprouxdev.arcencielplanning.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Local data source for User operations using SQLDelight
 */
class UserLocalDataSource(private val database: ArcEnCielDatabase) {

    private val queries = database.userDbQueries
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Get all users as a Flow
     */
    fun getAllUsers(): Flow<List<User>> {
        return queries.getAllUsers()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { userDbList ->
                userDbList.map { it.toUser() }
            }
    }

    /**
     * Get a single user by ID
     */
    suspend fun getUserById(id: String): User? {
        return queries.getUserById(id).executeAsOneOrNull()?.toUser()
    }

    /**
     * Get admin users
     */
    fun getAdminUsers(): Flow<List<User>> {
        return queries.getAdminUsers()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { userDbList ->
                userDbList.map { it.toUser() }
            }
    }

    /**
     * Get users by team
     */
    fun getUsersByTeam(team: Teams): Flow<List<User>> {
        return queries.getUsersByTeam(team.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { userDbList ->
                userDbList.map { it.toUser() }
            }
    }

    /**
     * Insert or replace a user
     */
    suspend fun insertOrReplace(user: User) {
        queries.insertOrReplace(
            id = user.id,
            name = user.name,
            isAdmin = user.isAdmin,
            imageUrl = user.imageUrl,
            teams = json.encodeToString(user.teams)
        )
    }

    /**
     * Insert or replace multiple users
     */
    suspend fun insertOrReplaceAll(users: List<User>) {
        queries.transaction {
            users.forEach { user ->
                queries.insertOrReplace(
                    id = user.id,
                    name = user.name,
                    isAdmin = user.isAdmin,
                    imageUrl = user.imageUrl,
                    teams = json.encodeToString(user.teams)
                )
            }
        }
    }

    /**
     * Delete a user by ID
     */
    suspend fun deleteById(id: String) {
        queries.deleteById(id)
    }

    /**
     * Delete all users
     */
    suspend fun deleteAll() {
        queries.deleteAll()
    }

    /**
     * Count total users
     */
    suspend fun countUsers(): Long {
        return queries.countUsers().executeAsOne()
    }

    /**
     * Extension function to convert UserDb to User
     */
    private fun com.aprouxdev.arcencielplanning.UserDb.toUser(): User {
        return User(
            id = this.id,
            name = this.name,
            isAdmin = this.isAdmin,
            imageUrl = this.imageUrl,
            teams = try {
                json.decodeFromString<List<Teams>>(this.teams)
            } catch (e: Exception) {
                emptyList()
            }
        )
    }
}
