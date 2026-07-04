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
