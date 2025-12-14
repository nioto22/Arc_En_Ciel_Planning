package com.aprouxdev.arcencielplanning.data.repository

import com.aprouxdev.arcencielplanning.data.local.CommentLocalDataSource
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.models.Comment
import com.aprouxdev.arcencielplanning.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of CommentRepository using offline-first approach
 */
class CommentRepositoryImpl(
    private val localDataSource: CommentLocalDataSource
) : CommentRepository {

    override fun getCommentsByEventId(eventId: String): Flow<List<Comment>> {
        return localDataSource.getCommentsByEventId(eventId)
    }

    override fun getCommentsByUserId(userId: String): Flow<List<Comment>> {
        return localDataSource.getCommentsByUserId(userId)
    }

    override suspend fun getCommentById(id: String): Comment? {
        return try {
            localDataSource.getCommentById(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveComment(comment: Comment): Result<Unit> {
        return try {
            localDataSource.insertOrReplace(comment)
            // TODO: Sync with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteComment(id: String): Result<Unit> {
        return try {
            localDataSource.deleteById(id)
            // TODO: Sync deletion with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncCommentsForEvent(eventId: String): Result<Unit> {
        // TODO: Implement sync with remote server
        return Result.success(Unit)
    }

    companion object {
        fun create(database: ArcEnCielDatabase): CommentRepositoryImpl {
            return CommentRepositoryImpl(
                localDataSource = CommentLocalDataSource(database)
            )
        }
    }
}
