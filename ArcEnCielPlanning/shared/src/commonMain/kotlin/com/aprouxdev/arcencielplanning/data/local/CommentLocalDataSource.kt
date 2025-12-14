package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.models.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local data source for Comment operations using SQLDelight
 */
class CommentLocalDataSource(private val database: ArcEnCielDatabase) {

    private val queries = database.commentDbQueries

    /**
     * Get all comments as a Flow
     */
    fun getAllComments(): Flow<List<Comment>> {
        return queries.getAllComments()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { commentDbList ->
                commentDbList.map { it.toComment() }
            }
    }

    /**
     * Get comments by event ID
     */
    fun getCommentsByEventId(eventId: String): Flow<List<Comment>> {
        return queries.getCommentsByEventId(eventId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { commentDbList ->
                commentDbList.map { it.toComment() }
            }
    }

    /**
     * Get comments by user ID
     */
    fun getCommentsByUserId(userId: String): Flow<List<Comment>> {
        return queries.getCommentsByUserId(userId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { commentDbList ->
                commentDbList.map { it.toComment() }
            }
    }

    /**
     * Get a single comment by ID
     */
    suspend fun getCommentById(id: String): Comment? {
        return queries.getCommentById(id).executeAsOneOrNull()?.toComment()
    }

    /**
     * Insert or replace a comment
     */
    suspend fun insertOrReplace(comment: Comment) {
        queries.insertOrReplace(
            id = comment.id,
            eventId = comment.eventId,
            userId = comment.userId,
            text = comment.text,
            date = comment.date
        )
    }

    /**
     * Insert or replace multiple comments
     */
    suspend fun insertOrReplaceAll(comments: List<Comment>) {
        queries.transaction {
            comments.forEach { comment ->
                queries.insertOrReplace(
                    id = comment.id,
                    eventId = comment.eventId,
                    userId = comment.userId,
                    text = comment.text,
                    date = comment.date
                )
            }
        }
    }

    /**
     * Delete a comment by ID
     */
    suspend fun deleteById(id: String) {
        queries.deleteById(id)
    }

    /**
     * Delete all comments for an event
     */
    suspend fun deleteByEventId(eventId: String) {
        queries.deleteByEventId(eventId)
    }

    /**
     * Delete all comments
     */
    suspend fun deleteAll() {
        queries.deleteAll()
    }

    /**
     * Count total comments
     */
    suspend fun countComments(): Long {
        return queries.countComments().executeAsOne()
    }

    /**
     * Extension function to convert CommentDb to Comment
     */
    private fun com.aprouxdev.arcencielplanning.CommentDb.toComment(): Comment {
        return Comment(
            id = this.id,
            eventId = this.eventId,
            userId = this.userId,
            text = this.text,
            date = this.date
        )
    }
}
