package com.aprouxdev.arcencielplanning.domain.repository

import com.aprouxdev.arcencielplanning.domain.models.Comment
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Comment operations
 */
interface CommentRepository {

    /**
     * Get all comments for an event
     */
    fun getCommentsByEventId(eventId: String): Flow<List<Comment>>

    /**
     * Get comments by user ID
     */
    fun getCommentsByUserId(userId: String): Flow<List<Comment>>

    /**
     * Get a single comment by ID
     */
    suspend fun getCommentById(id: String): Comment?

    /**
     * Save or update a comment
     */
    suspend fun saveComment(comment: Comment): Result<Unit>

    /**
     * Delete a comment by ID
     */
    suspend fun deleteComment(id: String): Result<Unit>

    /**
     * Sync comments for a specific event
     */
    suspend fun syncCommentsForEvent(eventId: String): Result<Unit>
}
