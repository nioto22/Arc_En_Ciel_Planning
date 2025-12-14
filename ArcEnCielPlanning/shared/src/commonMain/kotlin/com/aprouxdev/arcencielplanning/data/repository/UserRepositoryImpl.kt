package com.aprouxdev.arcencielplanning.data.repository

import com.aprouxdev.arcencielplanning.data.local.UserLocalDataSource
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.enums.Teams
import com.aprouxdev.arcencielplanning.domain.models.User
import com.aprouxdev.arcencielplanning.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of UserRepository using offline-first approach
 */
class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return localDataSource.getAllUsers()
    }

    override suspend fun getUserById(id: String): User? {
        return try {
            localDataSource.getUserById(id)
        } catch (e: Exception) {
            null
        }
    }

    override fun getAdminUsers(): Flow<List<User>> {
        return localDataSource.getAdminUsers()
    }

    override fun getUsersByTeam(team: Teams): Flow<List<User>> {
        return localDataSource.getUsersByTeam(team)
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            localDataSource.insertOrReplace(user)
            // TODO: Sync with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            localDataSource.deleteById(id)
            // TODO: Sync deletion with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncUsers(): Result<Unit> {
        // TODO: Implement sync with remote server
        return Result.success(Unit)
    }

    override suspend fun getCurrentUser(): User? {
        // TODO: Implement user session management
        return null
    }

    companion object {
        fun create(database: ArcEnCielDatabase): UserRepositoryImpl {
            return UserRepositoryImpl(
                localDataSource = UserLocalDataSource(database)
            )
        }
    }
}
