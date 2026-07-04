package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val itemId: String, // ID of the event, internship, job, or course
    val category: String, // "event", "internship", "job", "course"
    val bookmarkedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "course_progress")
data class CourseProgressEntity(
    @PrimaryKey val courseId: String,
    val isCompleted: Boolean,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_history")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String, // "user" or "model"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "event_subscription")
data class SubscriptionEntity(
    @PrimaryKey val id: String = "user_subscription",
    val email: String,
    val enableEmail: Boolean,
    val enableBrowser: Boolean,
    val prefGenAI: Boolean = true,
    val prefRobotics: Boolean = true,
    val prefWorkshops: Boolean = true,
    val prefFunding: Boolean = true,
    val isSubscribed: Boolean = true,
    val subscribedAt: Long = System.currentTimeMillis()
)

