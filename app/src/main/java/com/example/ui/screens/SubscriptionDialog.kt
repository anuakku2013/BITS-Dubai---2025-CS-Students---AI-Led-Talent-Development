package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.CareerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDialog(
    viewModel: CareerViewModel,
    onDismiss: () -> Unit
) {
    val subscription by viewModel.subscription.collectAsState()
    val context = LocalContext.current

    // Initialize states with existing subscription or defaults
    var emailText by remember { mutableStateOf(subscription?.email ?: "anuakku2013@gmail.com") }
    var emailError by remember { mutableStateOf(false) }

    var enableEmail by remember { mutableStateOf(subscription?.enableEmail ?: true) }
    var enableBrowser by remember { mutableStateOf(subscription?.enableBrowser ?: true) }

    var prefGenAI by remember { mutableStateOf(subscription?.prefGenAI ?: true) }
    var prefRobotics by remember { mutableStateOf(subscription?.prefRobotics ?: true) }
    var prefWorkshops by remember { mutableStateOf(subscription?.prefWorkshops ?: true) }
    var prefFunding by remember { mutableStateOf(subscription?.prefFunding ?: true) }

    // Trigger update on subscription load if database updates async
    LaunchedEffect(subscription) {
        subscription?.let { sub ->
            emailText = sub.email
            enableEmail = sub.enableEmail
            enableBrowser = sub.enableBrowser
            prefGenAI = sub.prefGenAI
            prefRobotics = sub.prefRobotics
            prefWorkshops = sub.prefWorkshops
            prefFunding = sub.prefFunding
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("subscription_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GeoBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with bell icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GeoEventBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Subscription Active",
                            tint = GeoPrimaryDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Dubai AI Alerts",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GeoPrimaryDark,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = if (subscription?.isSubscribed == true) "Active Subscription" else "Get Event Notifications",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (subscription?.isSubscribed == true) AIBluePrimary else TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Stay ahead by subscribing to browser or email notifications for upcoming AI events, developer meetups, hackathons, and funding programs in Dubai.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Input field
                Text(
                    text = "Your Email Address",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = GeoPrimaryDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = emailText,
                    onValueChange = {
                        emailText = it
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("subscription_email_input"),
                    placeholder = { Text("example@domain.com") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = TextSecondary
                        )
                    },
                    isError = emailError,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AIBluePrimary,
                        unfocusedBorderColor = GeoBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                if (emailError) {
                    Text(
                        text = "Please enter a valid email address.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Alert Channels Options
                Text(
                    text = "Notification Delivery Options",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = GeoPrimaryDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Email Channel Switch row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(0.5.dp, GeoBorder, RoundedCornerShape(12.dp))
                        .clickable { enableEmail = !enableEmail }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Email Notifications",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Receive summaries and priority invites",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = enableEmail,
                        onCheckedChange = { enableEmail = it },
                        modifier = Modifier.testTag("switch_email_notif"),
                        colors = SwitchDefaults.colors(checkedThumbColor = AIBluePrimary)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Browser Channel Switch row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(0.5.dp, GeoBorder, RoundedCornerShape(12.dp))
                        .clickable { enableBrowser = !enableBrowser }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "In-App & Browser Alerts",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Get real-time push alerts on events screen",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = enableBrowser,
                        onCheckedChange = { enableBrowser = it },
                        modifier = Modifier.testTag("switch_browser_notif"),
                        colors = SwitchDefaults.colors(checkedThumbColor = AIBluePrimary)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Categories Checkboxes
                Text(
                    text = "Event Categories Preferences",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = GeoPrimaryDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryCheckboxRow("Generative AI & Large Language Models", prefGenAI) { prefGenAI = it }
                    CategoryCheckboxRow("Robotics, IoT & Edge Computing", prefRobotics) { prefRobotics = it }
                    CategoryCheckboxRow("CS Workshops, Bootcamps & Hackathons", prefWorkshops) { prefWorkshops = it }
                    CategoryCheckboxRow("Incubator Projects & AED 100M Funding Alerts", prefFunding) { prefFunding = it }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left action: cancel / unsubscribe
                    if (subscription?.isSubscribed == true) {
                        OutlinedButton(
                            onClick = {
                                viewModel.removeSubscription()
                                android.widget.Toast.makeText(context, "Unsubscribed successfully", android.widget.Toast.LENGTH_SHORT).show()
                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("unsubscribe_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentOrange),
                            border = BorderStroke(1.dp, AccentOrange),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Unsubscribe",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1.0f)
                                .testTag("cancel_sub_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GeoBorder)
                        ) {
                            Text(
                                text = "Dismiss",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    // Right action: Subscribe / Save
                    Button(
                        onClick = {
                            if (emailText.isBlank() || emailError) {
                                emailError = true
                                return@Button
                            }
                            viewModel.saveSubscription(
                                email = emailText,
                                enableEmail = enableEmail,
                                enableBrowser = enableBrowser,
                                prefGenAI = prefGenAI,
                                prefRobotics = prefRobotics,
                                prefWorkshops = prefWorkshops,
                                prefFunding = prefFunding
                            )
                            android.widget.Toast.makeText(context, "Alert preferences saved successfully!", android.widget.Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("save_subscription_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = GeoPrimaryDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (subscription?.isSubscribed == true) "Save Changes" else "Subscribe",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = AIBluePrimary)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}
