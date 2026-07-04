package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.BuildConfig
import com.example.data.models.CareerData
import com.example.network.Content
import com.example.network.GenerateContentRequest
import com.example.network.Part
import com.example.network.RetrofitClient
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class QuizQuestion(
    val id: String,
    val text: String,
    val category: String, // "Python", "TensorFlow", "PyTorch", "RAG"
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

val quizQuestions = listOf(
    QuizQuestion(
        id = "q_python",
        text = "Which of the following in Python is most commonly used to efficiently manipulate large multi-dimensional arrays and matrices in scientific computing?",
        category = "Python",
        options = listOf(
            "A) Standard list of lists",
            "B) NumPy's ndarray",
            "C) Pandas DataFrame",
            "D) Dict key-value pairs"
        ),
        correctAnswerIndex = 1,
        explanation = "NumPy's ndarray provides high-performance, contiguous vector memory blocks and vectorized operations written in C, which is the foundational array structure for virtually all deep learning frameworks."
    ),
    QuizQuestion(
        id = "q_tensorflow",
        text = "In TensorFlow 2.x, what is the primary recommended high-level API used to build, compile, and train neural networks?",
        category = "TensorFlow",
        options = listOf(
            "A) tf.estimator",
            "B) tf.layers",
            "C) tf.keras",
            "D) tf.compat.v1"
        ),
        correctAnswerIndex = 2,
        explanation = "tf.keras is the official, user-friendly high-level API for TensorFlow 2.x, simplifying model creation, training, evaluation, and serialization."
    ),
    QuizQuestion(
        id = "q_pytorch_backward",
        text = "What is the function of the PyTorch 'backward()' call on a scalar loss tensor?",
        category = "PyTorch",
        options = listOf(
            "A) It resets model weight gradients to zero",
            "B) It executes a gradient descent weight optimization step",
            "C) It computes gradients of the loss with respect to leaf tensors using autograd",
            "D) It loads previous network states from a checkpoint"
        ),
        correctAnswerIndex = 2,
        explanation = "Calling backward() initiates PyTorch's automatic differentiation engine (autograd), computing the gradients of the scalar loss with respect to all leaf tensors that have requires_grad=True, storing them in their .grad attributes."
    ),
    QuizQuestion(
        id = "q_rag_vector",
        text = "What is the main role of a 'vector database' (like Pinecone, Milvus, or ChromaDB) in a standard Retrieval-Augmented Generation (RAG) pipeline?",
        category = "RAG",
        options = listOf(
            "A) To execute mathematical backpropagation to update the LLM's weights",
            "B) To store and search text chunk embeddings based on semantic similarity",
            "C) To convert text strings directly to speech audio files",
            "D) To compile and compress prompt templates into binary code"
        ),
        correctAnswerIndex = 1,
        explanation = "Vector databases store vector embeddings generated from document chunks and perform highly efficient nearest-neighbor search (semantic similarity) to retrieve relevant context chunks for the LLM prompt."
    ),
    QuizQuestion(
        id = "q_pytorch_grad",
        text = "In PyTorch, why is it critical to run 'optimizer.zero_grad()' before executing 'loss.backward()' in your training loop?",
        category = "PyTorch",
        options = listOf(
            "A) To clear old weight gradients, preventing them from accumulating across multiple backward passes",
            "B) To reset the model learning rate back to its initial start value",
            "C) To empty the GPU VRAM of all network parameters to prevent out-of-memory errors",
            "D) To force the loss value to strictly converge to zero"
        ),
        correctAnswerIndex = 0,
        explanation = "PyTorch accumulates gradients by default on subsequent backward() calls. Calling optimizer.zero_grad() clears these accumulated values from the previous batch, ensuring gradients represent only the current training step."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotSkillsQuizDialog(
    viewModel: CareerViewModel,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedPathId by viewModel.selectedPathId.collectAsState()
    val activePath = CareerData.paths.find { it.id == selectedPathId } ?: CareerData.paths.first()

    // Quiz States
    var currentStep by remember { mutableStateOf("start") } // "start", "quiz", "results"
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    val wrongQuestions = remember { mutableStateListOf<QuizQuestion>() }

    // Dynamic Feedback States
    var isGeneratingFeedback by remember { mutableStateOf(false) }
    var adviceText by remember { mutableStateOf("") }

    val currentQuestion = quizQuestions[currentQuestionIndex]

    // Fetch Dynamic Feedback from Gemini API
    fun fetchAdvisorFeedback() {
        isGeneratingFeedback = true
        adviceText = ""
        coroutineScope.launch {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                // Generate detailed fallback offline feedback
                adviceText = getOfflineFeedback(score, wrongQuestions, activePath.title)
                isGeneratingFeedback = false
                return@launch
            }

            val wrongCategories = wrongQuestions.map { it.category }.distinct()
            val wrongTopicsStr = if (wrongCategories.isEmpty()) "None! Perfect score!" else wrongCategories.joinToString(", ")
            
            val systemPrompt = """
                You are "AdvisorAI", an expert AI Career Coach specifically tailored for 2nd-year Computer Science students in Dubai.
                The student has just completed a "Hot Skills Assessment Quiz" on Python, TensorFlow, PyTorch, and Retrieval-Augmented Generation (RAG).
                Score: $score out of 5.
                Incorrect Areas: $wrongTopicsStr
                Selected Career Path: ${activePath.title}
                
                Generate a highly personalized, encouraging feedback of exactly 3-4 sentences.
                Suggest 1 or 2 specific courses from the student's curriculum to address their weaknesses:
                - For Python issues: Suggest solid foundations.
                - For PyTorch issues: Suggest "Practical Deep Learning for Coders (Fast.ai)".
                - For TensorFlow issues: Suggest "Machine Learning Specialization".
                - For RAG issues: Suggest "LangChain for LLM Application Development" or "Generative AI Fundamentals Specialization".
                
                Integrate references to Dubai AI internships (like PwC, TII, or Emirates Group) or local ecosystem developments. Keep the advice actionable, structured, professional, and exciting!
            """.trimIndent()

            val prompt = "Generate my personalized roadmap learning action plan feedback."

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
            )

            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.service.generateContent(apiKey, request)
                }
                val replyText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!replyText.isNullOrEmpty()) {
                    adviceText = replyText.trim()
                } else {
                    adviceText = getOfflineFeedback(score, wrongQuestions, activePath.title)
                }
            } catch (e: Exception) {
                adviceText = getOfflineFeedback(score, wrongQuestions, activePath.title)
            } finally {
                isGeneratingFeedback = false
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .padding(vertical = 12.dp)
                .testTag("hot_skills_quiz_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GeoBackground),
            border = BorderStroke(1.dp, GeoBorder.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GeoProgressBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Quiz Icon",
                                tint = GeoProgressText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Hot Skills Assessment",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = GeoPrimaryDark
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("quiz_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (currentStep) {
                    "start" -> {
                        // Welcome layout
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(GeoEventBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Brain",
                                    tint = GeoPrimaryDark,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Test Your AI Readiness",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = GeoPrimaryDark,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Assess your current level in Python, PyTorch, TensorFlow, & RAG — the hottest competencies requested by Dubai AI hubs (DIFC, TII, PwC). Unlock AI-crafted recommendations to optimize your course roadmap!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Highlight of skills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("🐍 Python", "🔥 PyTorch", "💡 TensorFlow", "🤖 RAG").forEach { skill ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(GeoSurfaceVariant)
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = skill,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = GeoPrimaryDark,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    score = 0
                                    wrongQuestions.clear()
                                    currentQuestionIndex = 0
                                    selectedOptionIndex = null
                                    isAnswerChecked = false
                                    currentStep = "quiz"
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("start_quiz_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = GeoPrimaryDark),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Start 5-Question Quiz",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    "quiz" -> {
                        // Quiz in Progress
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Progress bar & label
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Question ${currentQuestionIndex + 1} of ${quizQuestions.size}",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AIBluePrimary
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when (currentQuestion.category) {
                                                "Python" -> GeoEventBg
                                                "PyTorch" -> NeonPurple.copy(alpha = 0.15f)
                                                "TensorFlow" -> AccentOrange.copy(alpha = 0.15f)
                                                else -> GeoProgressBg
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = currentQuestion.category,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = when (currentQuestion.category) {
                                                "Python" -> GeoPrimaryDark
                                                "PyTorch" -> NeonPurple
                                                "TensorFlow" -> AccentOrange
                                                else -> GeoProgressText
                                            }
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Graphic Progress Line
                            LinearProgressIndicator(
                                progress = { (currentQuestionIndex + 1).toFloat() / quizQuestions.size.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = GeoProgressIndicator,
                                trackColor = GeoSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Question Text
                            Text(
                                text = currentQuestion.text,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 22.sp
                                ),
                                color = TextPrimary,
                                modifier = Modifier.testTag("quiz_question_text")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Options List
                            currentQuestion.options.forEachIndexed { index, optionText ->
                                val isSelected = selectedOptionIndex == index
                                val isCorrect = index == currentQuestion.correctAnswerIndex

                                val optionBgColor = when {
                                    isAnswerChecked && isCorrect -> Color(0xFFE8F5E9) // soft green for correct
                                    isAnswerChecked && isSelected && !isCorrect -> Color(0xFFFFEBEE) // soft red for selected wrong
                                    isSelected -> GeoProgressBg.copy(alpha = 0.5f)
                                    else -> Color.White
                                }

                                val optionBorderColor = when {
                                    isAnswerChecked && isCorrect -> Color(0xFF4CAF50)
                                    isAnswerChecked && isSelected && !isCorrect -> Color(0xFFE53935)
                                    isSelected -> GeoProgressIndicator
                                    else -> GeoBorder.copy(alpha = 0.5f)
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable(enabled = !isAnswerChecked) {
                                            selectedOptionIndex = index
                                        }
                                        .testTag("quiz_option_$index"),
                                    colors = CardDefaults.cardColors(containerColor = optionBgColor),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.5.dp, optionBorderColor)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { if (!isAnswerChecked) selectedOptionIndex = index },
                                            enabled = !isAnswerChecked,
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = GeoProgressIndicator,
                                                unselectedColor = TextSecondary.copy(alpha = 0.5f)
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = if (isAnswerChecked && isCorrect) Color(0xFF1B5E20)
                                                    else if (isAnswerChecked && isSelected && !isCorrect) Color(0xFFB71C1C)
                                                    else TextPrimary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Explanation and results box after checking
                            if (isAnswerChecked) {
                                val wasCorrect = selectedOptionIndex == currentQuestion.correctAnswerIndex
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (wasCorrect) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, if (wasCorrect) Color(0x334CAF50) else Color(0x33FF9800))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (wasCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                                                contentDescription = if (wasCorrect) "Correct" else "Incorrect",
                                                tint = if (wasCorrect) Color(0xFF2E7D32) else Color(0xFFEF6C00),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (wasCorrect) "Correct Answer!" else "Incorrect!",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = if (wasCorrect) Color(0xFF2E7D32) else Color(0xFFE65100)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = currentQuestion.explanation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action buttons (Check Answer vs Next Question)
                            if (!isAnswerChecked) {
                                Button(
                                    onClick = {
                                        if (selectedOptionIndex != null) {
                                            isAnswerChecked = true
                                            if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
                                                score++
                                            } else {
                                                wrongQuestions.add(currentQuestion)
                                            }
                                        }
                                    },
                                    enabled = selectedOptionIndex != null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("check_answer_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = GeoPrimaryDark),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Check Answer",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        if (currentQuestionIndex + 1 < quizQuestions.size) {
                                            currentQuestionIndex++
                                            selectedOptionIndex = null
                                            isAnswerChecked = false
                                        } else {
                                            fetchAdvisorFeedback()
                                            currentStep = "results"
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("next_question_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = GeoProgressIndicator),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (currentQuestionIndex + 1 < quizQuestions.size) "Next Question" else "Finish & View Roadmap Feedback",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    "results" -> {
                        // Results Screen with dynamic Advisor AI Feedback
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Assessment Completed!",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = AIBluePrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Large score badge
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(GeoProgressBg)
                                    .border(2.dp, GeoProgressBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$score / 5",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Black,
                                            fontSize = 32.sp
                                        ),
                                        color = GeoProgressText
                                    )
                                    Text(
                                        text = "Correct",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GeoProgressText.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = when (score) {
                                    5 -> "🏆 Master of Hot AI Skills"
                                    4 -> "⚡ Advanced AI Learner"
                                    3 -> "🌱 Budding AI Enthusiast"
                                    else -> "📚 AI Explorer"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = GeoPrimaryDark
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // AdvisorAI Custom Feedback card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = GeoSurfaceVariant),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, GeoBorder.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Psychology,
                                                contentDescription = "Advisor AI Icon",
                                                tint = GeoProgressIndicator,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "AdvisorAI Learning Feedback",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = GeoPrimaryDark
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (isGeneratingFeedback) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(
                                                color = GeoProgressIndicator,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Analyzing roadmap alignment...",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextSecondary
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = adviceText,
                                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        score = 0
                                        wrongQuestions.clear()
                                        currentQuestionIndex = 0
                                        selectedOptionIndex = null
                                        isAnswerChecked = false
                                        currentStep = "quiz"
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("retake_quiz_button"),
                                    border = BorderStroke(1.dp, GeoBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GeoPrimaryDark)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Restart", modifier = Modifier.size(16.dp))
                                        Text("Retake Quiz", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                    }
                                }

                                Button(
                                    onClick = {
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("results_close_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = GeoPrimaryDark),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "Close & Study",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Offline fallback helper to give spectacular, highly localized action feedback instantly
fun getOfflineFeedback(score: Int, wrongQuestions: List<QuizQuestion>, activePathTitle: String): String {
    val wrongCategories = wrongQuestions.map { it.category }.distinct()
    
    val feedbackBase = when (score) {
        5 -> "Spectacular! You scored a perfect 5/5! Your understanding of Python, PyTorch, TensorFlow, and RAG architectures is outstanding. You are fully equipped to apply for competitive local roles like PwC's Machine Learning Intern or G42's Associate Developer."
        4 -> "Excellent job! You scored 4/5. You have a solid command of deep learning basics. "
        3 -> "Good start! You scored 3/5. You have the foundational knowledge, but some advanced areas need polishing to be ready for Dubai internships. "
        else -> "You scored $score/5. Don't worry! This is the perfect baseline for a 2nd-year CS student to identify gaps and supercharge their roadmap. "
    }

    val actionPlan = if (wrongCategories.isEmpty()) {
        " Your immediate next milestone should be to complete a final project—such as deploying a localized Arabic Falcon-based LLM agent. Continue maintaining your excellent curriculum progress!"
    } else {
        val courseRecommendations = wrongCategories.map { cat ->
            when (cat) {
                "Python" -> "Python core fundamentals"
                "PyTorch" -> "'Practical Deep Learning for Coders' (Fast.ai) to master autograd loops"
                "TensorFlow" -> "Stanford's 'Machine Learning Specialization' to consolidate neural layers"
                "RAG" -> "DeepLearning.AI's 'LangChain for LLM Application Development' to understand Vector DB querying"
                else -> ""
            }
        }.filter { it.isNotEmpty() }.joinToString(" and ")

        " To solidify your learning roadmap under the '$activePathTitle' track, we highly recommend focusing on $courseRecommendations. This will directly boost your readiness for upcoming Dubai AI assemblies and hackathons."
    }

    return feedbackBase + actionPlan
}
