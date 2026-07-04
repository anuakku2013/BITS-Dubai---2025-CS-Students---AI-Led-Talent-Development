package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.viewmodel.CareerViewModel
import com.example.ui.screens.BookmarkedBottomSheetContent
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.JobsScreen
import com.example.ui.screens.MentorScreen
import com.example.ui.screens.RoadmapScreen
import com.example.ui.screens.SubscriptionDialog
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AIBluePrimary
import com.example.ui.theme.GeoBackground
import com.example.ui.theme.GeoPrimaryDark
import com.example.ui.theme.GeoNavIndicator
import com.example.ui.theme.GeoProgressIndicator
import com.example.ui.theme.GeoEventBg
import com.example.ui.theme.GeoTextSecondary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.GeoSurfaceVariant
import com.example.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent() {
    val viewModel: CareerViewModel = viewModel()
    val activeTab by viewModel.activeTab.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()

    var showBookmarksSheet by remember { mutableStateOf(false) }
    var showSubscriptionDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "CS STUDENT • YEAR 2",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                color = TextSecondary
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI.Dubai",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium.copy(color = GeoPrimaryDark)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(TextSecondary.copy(alpha = 0.5f), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (activeTab) {
                                    0 -> "Dashboard"
                                    1 -> "Roadmap"
                                    2 -> "Dubai Pulse"
                                    3 -> "Placement"
                                    else -> "AdvisorAI"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = GeoProgressIndicator
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Profile Circle Avatar
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GeoEventBg)
                            .clickable { showSubscriptionDialog = true }
                            .testTag("profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AS",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GeoPrimaryDark
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Central bookmarks button on the top toolbar
                    IconButton(
                        onClick = { showBookmarksSheet = true },
                        modifier = Modifier.testTag("bookmarks_toolbar_button")
                    ) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Bookmarks Panel",
                                tint = if (bookmarks.isNotEmpty()) AccentOrange else GeoPrimaryDark
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GeoBackground,
                    titleContentColor = GeoPrimaryDark,
                    actionIconContentColor = GeoPrimaryDark
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = GeoSurfaceVariant,
                tonalElevation = 8.dp
            ) {
                // Dashboard Tab
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { viewModel.setActiveTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == 0) Icons.Default.Dashboard else Icons.Outlined.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = { Text("Home", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GeoNavIndicator,
                        selectedIconColor = GeoPrimaryDark,
                        selectedTextColor = GeoPrimaryDark,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                // Curriculum / Roadmap Tab
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { viewModel.setActiveTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == 1) Icons.Default.Map else Icons.Outlined.Map,
                            contentDescription = "Roadmap"
                        )
                    },
                    label = { Text("Roadmap", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GeoNavIndicator,
                        selectedIconColor = GeoPrimaryDark,
                        selectedTextColor = GeoPrimaryDark,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_roadmap")
                )

                // Events & Internships Tab
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { viewModel.setActiveTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == 2) Icons.Default.CalendarMonth else Icons.Outlined.CalendarMonth,
                            contentDescription = "Events & Internships"
                        )
                    },
                    label = { Text("Dubai Pulse", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GeoNavIndicator,
                        selectedIconColor = GeoPrimaryDark,
                        selectedTextColor = GeoPrimaryDark,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_events")
                )

                // Placement (Jobs & Skills) Tab
                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { viewModel.setActiveTab(3) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == 3) Icons.Default.BusinessCenter else Icons.Outlined.BusinessCenter,
                            contentDescription = "Jobs & Skills"
                        )
                    },
                    label = { Text("Placement", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GeoNavIndicator,
                        selectedIconColor = GeoPrimaryDark,
                        selectedTextColor = GeoPrimaryDark,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_jobs")
                )

                // AI Counselor Tab
                NavigationBarItem(
                    selected = activeTab == 4,
                    onClick = { viewModel.setActiveTab(4) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == 4) Icons.Default.Psychology else Icons.Outlined.Psychology,
                            contentDescription = "AI Mentor"
                        )
                    },
                    label = { Text("AdvisorAI", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GeoNavIndicator,
                        selectedIconColor = GeoPrimaryDark,
                        selectedTextColor = GeoPrimaryDark,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_mentor")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                0 -> DashboardScreen(viewModel = viewModel)
                1 -> RoadmapScreen(viewModel = viewModel)
                2 -> EventsScreen(viewModel = viewModel)
                3 -> JobsScreen(viewModel = viewModel)
                else -> MentorScreen(viewModel = viewModel)
            }
        }

        // Bottom sheet displaying bookmarked items
        if (showBookmarksSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBookmarksSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                BookmarkedBottomSheetContent(
                    viewModel = viewModel,
                    onDismiss = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBookmarksSheet = false
                            }
                        }
                    }
                )
            }
        }

        // Dialog for Event Subscription Preferences
        if (showSubscriptionDialog) {
            SubscriptionDialog(
                viewModel = viewModel,
                onDismiss = { showSubscriptionDialog = false }
            )
        }
    }
}
