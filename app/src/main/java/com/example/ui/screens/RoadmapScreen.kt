package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CareerData
import com.example.data.models.openLink
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerViewModel

@Composable
fun RoadmapScreen(viewModel: CareerViewModel) {
    val context = LocalContext.current
    val progress by viewModel.courseProgress.collectAsState()
    val selectedPathId by viewModel.selectedPathId.collectAsState()

    val currentPath = CareerData.paths.find { it.id == selectedPathId } ?: CareerData.paths.first()
    
    // Filter the general course list based on what the current roadmap path requires
    val requiredCourseIds = currentPath.requiredCourses
    val roadmapCourses = CareerData.courses.filter { it.id in requiredCourseIds }

    val completedCourseCount = progress.count { it.isCompleted && it.courseId in requiredCourseIds }
    val totalRequiredCourses = roadmapCourses.size
    val roadmapPercent = if (totalRequiredCourses > 0) {
        (completedCourseCount.toFloat() / totalRequiredCourses.toFloat() * 100).toInt()
    } else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("roadmap_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Path Heading & Stats Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GeoProgressBg),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, GeoProgressBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = "Active Path",
                                    tint = GeoProgressText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = currentPath.title + " Path",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = GeoProgressText
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A complete structured set of high-quality free courses curated to place you directly in Dubai's AI firms.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Readiness Progress Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Placement Readiness:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "$roadmapPercent%",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (roadmapPercent >= 75) Color.Green else if (roadmapPercent >= 40) AccentOrange else AIBluePrimary
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when {
                                roadmapPercent == 100 -> "🚀 100% Placed! You are highly recommended for all local AI internships!"
                                roadmapPercent >= 75 -> "🔥 Amazing! Apply for PwC or TII internships with high confidence!"
                                roadmapPercent >= 50 -> "⚡ Great work! Keep studying to unlock elite Dubai developer roles."
                                else -> "📚 Start completing courses below to build your portfolio."
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // 1.5. Hot Skills Assessment Banner Card
        item {
            var showQuizDialog by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("roadmap_quiz_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceVariant),
                border = BorderStroke(1.dp, GeoBorder.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GeoEventBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Quiz Icon",
                            tint = GeoPrimaryDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Test Your AI Readiness",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = GeoPrimaryDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Take a 5-question quiz on Python, PyTorch & TensorFlow. Unlock AI-crafted roadmap recommendations!",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            lineHeight = 14.sp
                        )
                    }

                    Button(
                        onClick = { showQuizDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GeoPrimaryDark),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        modifier = Modifier.testTag("roadmap_banner_quiz_button")
                    ) {
                        Text(
                            text = "Start Quiz",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            if (showQuizDialog) {
                HotSkillsQuizDialog(
                    viewModel = viewModel,
                    onDismiss = { showQuizDialog = false }
                )
            }
        }

        // 2. Section: Course Timeline
        item {
            Text(
                text = "Course Curriculum",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // 3. Render Curriculum Courses
        items(roadmapCourses) { course ->
            val isCompleted = progress.any { it.courseId == course.id && it.isCompleted }
            
            // Checkbox color animation
            val checkBoxBg by animateColorAsState(
                targetValue = if (isCompleted) Color(0xFF00C853) else Color.Transparent,
                label = "check_box_bg"
            )
            val checkBoxBorder by animateColorAsState(
                targetValue = if (isCompleted) Color(0xFF00C853) else TextSecondary.copy(alpha = 0.5f),
                label = "check_box_border"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                border = if (isCompleted) BorderStroke(1.dp, Color(0xFF00C853).copy(alpha = 0.3f)) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Custom interactive circular checkbox
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(checkBoxBg)
                            .border(1.5.dp, checkBoxBorder, CircleShape)
                            .clickable { viewModel.toggleCourseCompletion(course.id) }
                            .testTag("course_checkbox_${course.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (course.category) {
                                            "Foundation" -> AIBluePrimary.copy(alpha = 0.15f)
                                            "Machine Learning" -> AIBlueSecondary.copy(alpha = 0.15f)
                                            "Deep Learning" -> NeonPurple.copy(alpha = 0.15f)
                                            else -> AccentOrange.copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = course.category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = when (course.category) {
                                            "Foundation" -> AIBluePrimary
                                            "Machine Learning" -> AIBlueSecondary
                                            "Deep Learning" -> NeonPurple
                                            else -> AccentOrange
                                        }
                                    )
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = AccentOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = course.rating.toString(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) TextPrimary.copy(alpha = 0.6f) else TextPrimary
                            )
                        )

                        Text(
                            text = "Provided by: " + course.provider,
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCompleted) TextSecondary.copy(alpha = 0.6f) else TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⏱️ " + course.duration,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Button(
                                onClick = {
                                    openLink(context, course.link)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = AIBluePrimary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("Free Course", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                    Icon(
                                        imageVector = Icons.Default.Launch,
                                        contentDescription = "Open course",
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Course completed border is handled natively by BorderStroke
