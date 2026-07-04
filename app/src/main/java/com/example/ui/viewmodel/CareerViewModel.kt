package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.ChatMessageEntity
import com.example.data.local.CourseProgressEntity
import com.example.data.models.CareerData
import com.example.data.models.AIEvent
import com.example.data.models.AIInternship
import com.example.data.models.AIJob
import com.example.data.models.FreeCourse
import com.example.data.models.HotSkill
import com.example.data.models.CareerPath
import com.example.data.repository.CareerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CareerViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = CareerRepository(database.careerDao())

    // Backing flows from Room
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courseProgress: StateFlow<List<CourseProgressEntity>> = repository.courseProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatHistory: StateFlow<List<ChatMessageEntity>> = repository.chatHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subscription: StateFlow<com.example.data.local.SubscriptionEntity?> = repository.subscriptionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveSubscription(
        email: String,
        enableEmail: Boolean,
        enableBrowser: Boolean,
        prefGenAI: Boolean = true,
        prefRobotics: Boolean = true,
        prefWorkshops: Boolean = true,
        prefFunding: Boolean = true
    ) {
        viewModelScope.launch {
            repository.saveSubscription(
                com.example.data.local.SubscriptionEntity(
                    email = email,
                    enableEmail = enableEmail,
                    enableBrowser = enableBrowser,
                    prefGenAI = prefGenAI,
                    prefRobotics = prefRobotics,
                    prefWorkshops = prefWorkshops,
                    prefFunding = prefFunding,
                    isSubscribed = true
                )
            )
        }
    }

    fun removeSubscription() {
        viewModelScope.launch {
            repository.removeSubscription()
        }
    }

    // Active Tab Navigation
    private val _activeTab = MutableStateFlow(0) // 0: Dashboard, 1: Roadmap, 2: Events & Interns, 3: Jobs & Skills, 4: AI Advisor
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    // Selected career path (helps students customize their dashboard and roadmap!)
    private val _selectedPathId = MutableStateFlow<String?>("path_gen_ai") // default to Gen AI
    val selectedPathId: StateFlow<String?> = _selectedPathId.asStateFlow()

    // Feeds state
    private val _aiFeeds = MutableStateFlow<List<com.example.data.models.AIFeedItem>>(com.example.data.models.CareerData.defaultFeeds)
    val aiFeeds: StateFlow<List<com.example.data.models.AIFeedItem>> = _aiFeeds.asStateFlow()

    private val _isRefreshingFeeds = MutableStateFlow(false)
    val isRefreshingFeeds: StateFlow<Boolean> = _isRefreshingFeeds.asStateFlow()

    fun refreshAIFeeds() {
        if (_isRefreshingFeeds.value) return
        viewModelScope.launch {
            _isRefreshingFeeds.value = true
            try {
                val generated = repository.fetchLatestAIFeeds()
                if (generated.isNotEmpty()) {
                    _aiFeeds.value = generated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isRefreshingFeeds.value = false
            }
        }
    }

    // Loading states
    private val _isGeneratingReply = MutableStateFlow(false)
    val isGeneratingReply: StateFlow<Boolean> = _isGeneratingReply.asStateFlow()

    // Text inputs
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setActiveTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun selectPath(pathId: String?) {
        _selectedPathId.value = pathId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Toggle Bookmarks
    fun toggleBookmark(itemId: String, category: String) {
        viewModelScope.launch {
            val isBookmarked = bookmarks.value.any { it.itemId == itemId }
            repository.toggleBookmark(itemId, category, isBookmarked)
        }
    }

    // Toggle Course Completion
    fun toggleCourseCompletion(courseId: String) {
        viewModelScope.launch {
            val isCompleted = courseProgress.value.any { it.courseId == courseId && it.isCompleted }
            repository.setCourseProgress(courseId, !isCompleted)
        }
    }

    // Send chat message to Advisor
    fun sendChatMessage(messageContent: String) {
        if (messageContent.trim().isEmpty() || _isGeneratingReply.value) return

        viewModelScope.launch {
            _isGeneratingReply.value = true
            // 1. Add user message
            repository.addUserMessage(messageContent)
            
            // 2. Fetch full updated history to feed the model
            val currentHistory = repository.chatHistory.stateIn(this).value
            
            // 3. Ask Gemini and save answer
            repository.generateAdvisorReply(currentHistory)
            _isGeneratingReply.value = false
        }
    }

    // Clear chat conversation
    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    init {
        // Pre-populate chat history with a welcoming message from the AI Career Mentor if it is empty!
        viewModelScope.launch {
            repository.chatHistory.collect { history ->
                if (history.isEmpty()) {
                    database.careerDao().insertChatMessage(
                        ChatMessageEntity(
                            role = "model",
                            content = "Marhaban! I am AdvisorAI, your dedicated Career Coach. As a 2nd-year Computer Science student, this is the perfect time to build your roadmap, secure internships, and gain hot skills in Artificial Intelligence here in Dubai.\n\nAsk me anything! For example:\n• What are the most in-demand AI skills in DIFC?\n• How can I prepare for the upcoming PwC internship?\n• Can you suggest projects to build with my PyTorch skills?"
                        )
                    )
                }
            }
        }
        // Fetch AI Feeds on launch
        refreshAIFeeds()
    }
}
