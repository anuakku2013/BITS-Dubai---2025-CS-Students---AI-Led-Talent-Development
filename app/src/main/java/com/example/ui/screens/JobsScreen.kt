package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CareerData
import com.example.data.models.openLink
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobsScreen(viewModel: CareerViewModel) {
    val context = LocalContext.current
    val bookmarks by viewModel.bookmarks.collectAsState()
    val progress by viewModel.courseProgress.collectAsState()

    var activeSubTab by remember { mutableStateOf("jobs") } // "jobs" or "skills"
    var expandedJobId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("jobs_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Selector tab bar (Geometric Balance Style)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Jobs for Freshers Pill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (activeSubTab == "jobs") GeoPrimaryDark else Color.White)
                        .then(
                            if (activeSubTab != "jobs") Modifier.background(Color.White).border(1.dp, GeoBorder, RoundedCornerShape(16.dp))
                            else Modifier
                        )
                        .clickable { activeSubTab = "jobs" }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BusinessCenter,
                            contentDescription = "Jobs Icon",
                            tint = if (activeSubTab == "jobs") Color.White else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Fresh Graduate Jobs",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (activeSubTab == "jobs") Color.White else GeoPrimaryDark
                        )
                    }
                }

                // Hot Skills Pill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (activeSubTab == "skills") GeoPrimaryDark else Color.White)
                        .then(
                            if (activeSubTab != "skills") Modifier.background(Color.White).border(1.dp, GeoBorder, RoundedCornerShape(16.dp))
                            else Modifier
                        )
                        .clickable { activeSubTab = "skills" }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Skills Icon",
                            tint = if (activeSubTab == "skills") Color.White else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Hot AI Skills",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (activeSubTab == "skills") Color.White else GeoPrimaryDark
                        )
                    }
                }
            }
        }

        // 2. Render contents
        if (activeSubTab == "jobs") {
            items(CareerData.jobs) { job ->
                val isBookmarked = bookmarks.any { it.itemId == job.id }
                val isExpanded = expandedJobId == job.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = job.company,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = AIBluePrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = job.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = { viewModel.toggleBookmark(job.id, "job") },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("bookmark_job_${job.id}")
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (isBookmarked) AccentOrange else TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Location, Experience, and Salary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = job.location,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Exp",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = job.experienceRequired,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Estimated Salary: " + job.salaryEstimate,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AccentOrange
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Skills Tags flow row
                        Text(
                            text = "Required Competencies:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            job.skillsNeeded.forEach { skillName ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = skillName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        // Expandable job description
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedJobId = if (isExpanded) null else job.id }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isExpanded) "Hide details" else "Show description & application",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AIBlueSecondary
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand",
                                tint = AIBlueSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                Text(
                                    text = "Role Overview:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = job.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                    openLink(context, job.applyLink)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = AIBluePrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("Apply Directly", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
                                        Icon(imageVector = Icons.Default.Launch, contentDescription = "Apply", tint = Color.Black, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Hot Skills Assessment Banner Card
            item {
                var showQuizDialog by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("jobs_skills_quiz_banner"),
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
                                text = "Assess Hot AI Skills",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = GeoPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Take a short interactive quiz assessing your Python, TensorFlow, and PyTorch expertise. Receive personalized roadmap guidance!",
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
                            modifier = Modifier.testTag("jobs_skills_banner_quiz_button")
                        ) {
                            Text(
                                text = "Take Quiz",
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

            // Hot Skills list
            items(CareerData.hotSkills) { skill ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(AIBluePrimary.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = "Skill Icon",
                                        tint = AIBluePrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = skill.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }

                            // Score indicator
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AccentOrange.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = "Hot",
                                        tint = AccentOrange,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${skill.importanceScore}/10 Demand",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AccentOrange
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = skill.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Market Value Boost: ",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = skill.salaryBoost,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.Green
                            )
                        }

                        // Project Suggestion Box
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "💡 Portfolio Project Idea:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AIBluePrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = skill.projectIdea,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                            }
                        }

                        // Recommended Courses
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Syllabus Courses To Master This Skill:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            skill.recommendedCourses.forEach { courseId ->
                                val course = CareerData.courses.find { it.id == courseId }
                                if (course != null) {
                                    val isDone = progress.any { it.courseId == courseId && it.isCompleted }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isDone) Color(0xFF00C853).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                            .border(1.dp, if (isDone) Color(0xFF00C853) else Color.Transparent, RoundedCornerShape(6.dp))
                                            .clickable { viewModel.setActiveTab(1) } // Roadmap tab
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (course.title.length > 20) course.title.take(18) + "..." else course.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isDone) Color(0xFF00C853) else TextPrimary
                                        )
                                    }
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
