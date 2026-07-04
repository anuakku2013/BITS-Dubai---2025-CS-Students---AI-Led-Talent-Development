package com.example.data.repository

import com.example.BuildConfig
import com.example.data.local.BookmarkEntity
import com.example.data.local.CareerDao
import com.example.data.local.ChatMessageEntity
import com.example.data.local.CourseProgressEntity
import com.example.data.models.CareerData
import com.example.network.Content
import com.example.network.GenerateContentRequest
import com.example.network.Part
import com.example.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CareerRepository(private val careerDao: CareerDao) {

    // Local Data Streams
    val bookmarks: Flow<List<BookmarkEntity>> = careerDao.getAllBookmarks()
    val courseProgress: Flow<List<CourseProgressEntity>> = careerDao.getAllCourseProgress()
    val chatHistory: Flow<List<ChatMessageEntity>> = careerDao.getChatMessages()

    // Bookmark operations
    suspend fun toggleBookmark(itemId: String, category: String, isCurrentlyBookmarked: Boolean) {
        withContext(Dispatchers.IO) {
            if (isCurrentlyBookmarked) {
                careerDao.deleteBookmarkById(itemId)
            } else {
                careerDao.insertBookmark(BookmarkEntity(itemId = itemId, category = category))
            }
        }
    }

    // Course progress operations
    suspend fun setCourseProgress(courseId: String, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            careerDao.insertCourseProgress(CourseProgressEntity(courseId = courseId, isCompleted = isCompleted))
        }
    }

    // Chat operations
    suspend fun addUserMessage(content: String) {
        withContext(Dispatchers.IO) {
            careerDao.insertChatMessage(ChatMessageEntity(role = "user", content = content))
        }
    }

    suspend fun clearChat() {
        withContext(Dispatchers.IO) {
            careerDao.clearChatHistory()
        }
    }

    // Chat with Gemini API & Save Response
    suspend fun generateAdvisorReply(history: List<ChatMessageEntity>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key not configured. Please add GEMINI_API_KEY in the AI Studio Secrets panel."
        }

        // Map database chat history to Gemini Content parts
        val contents = history.map {
            Content(
                parts = listOf(Part(text = it.content)),
                role = if (it.role == "user") "user" else "model"
            )
        }

        val systemPrompt = """
            You are "AdvisorAI", an expert AI Career Coach specifically tailored for 2nd-year Computer Science students in Dubai.
            Your purpose is to help them navigate:
            1. Upcoming local Dubai AI Events & Festivals (such as Dubai AI & Web3 Festival, Dubai Assembly for Generative AI, local university hackathons).
            2. AI Internship programs online and locally in Dubai.
            3. Establishing an AI Career build roadmap using free premium online courses.
            4. Transitioning into entry-level jobs for fresh graduates in Dubai.
            5. Mastering highly demanded "Hot Skills" (such as RAG, PyTorch, Vector Databases, and MLOps).
            
            Always keep your tone professional, encouraging, practical, and highly localized to Dubai and the UAE tech ecosystem (DIFC, Dubai Internet City, Area 2071, Dtec, etc.).
            Reference actual local tech developments, Falcon LLM (by TII in UAE), or specific local universities where relevant.
            Provide actionable milestones. Keep answers relatively concise and highly structured (using lists and bold text) so they are easy to read in a mobile chat interface.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = contents,
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val replyText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "I apologize, but I am unable to formulate a response at the moment. Please try again."
            
            // Save the reply into database
            careerDao.insertChatMessage(ChatMessageEntity(role = "model", content = replyText))
            replyText
        } catch (e: Exception) {
            val errorMessage = "Error: Unable to connect to mentor API (${e.localizedMessage}). Please check your internet connection."
            careerDao.insertChatMessage(ChatMessageEntity(role = "model", content = errorMessage))
            errorMessage
        }
    }
}
