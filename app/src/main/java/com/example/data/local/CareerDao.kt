package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CareerDao {
    // Bookmark Operations
    @Query("SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE itemId = :itemId")
    suspend fun deleteBookmarkById(itemId: String)

    // Course Progress Operations
    @Query("SELECT * FROM course_progress")
    fun getAllCourseProgress(): Flow<List<CourseProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseProgress(progress: CourseProgressEntity)

    // Chat History Operations
    @Query("SELECT * FROM chat_history ORDER BY id ASC")
    fun getChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_history")
    suspend fun clearChatHistory()

    // Subscription Operations
    @Query("SELECT * FROM event_subscription WHERE id = 'user_subscription' LIMIT 1")
    fun getSubscriptionFlow(): Flow<SubscriptionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: SubscriptionEntity)

    @Query("DELETE FROM event_subscription WHERE id = 'user_subscription'")
    suspend fun deleteSubscription()
}
