package com.lunaflow.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lunaflow.ui.components.ReminderCard
import com.lunaflow.ui.components.WorkoutCard
import com.lunaflow.ui.theme.*
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCalendar: () -> Unit,
    onNavigateToWorkouts: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "LunaFlow",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PinkPrimary
                    )
                },
                actions = {
                    IconButton(onClick = { /* Open settings */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = PinkPrimary
                        )
                    }
                    IconButton(onClick = { /* Open profile */ }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = PinkPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PinkBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Card
            item {
                WelcomeCard(
                    name = uiState.userName,
                    cycleDay = uiState.currentCycleDay,
                    nextPrediction = uiState.nextPeriodDate
                )
            }
            
            // Reminder Card
            item {
                ReminderCard(
                    daysUntilPeriod = uiState.daysUntilPeriod,
                    predictedDate = uiState.nextPeriodDate,
                    onDismiss = { viewModel.dismissReminder() }
                )
            }
            
            // Quick Stats
            item {
                QuickStatsRow(
                    cycleLength = uiState.averageCycleLength,
                    periodLength = uiState.averagePeriodLength,
                    daysUntilNext = uiState.daysUntilPeriod
                )
            }
            
            // Today's Recommendation
            item {
                DailyRecommendationCard(
                    phase = uiState.currentPhase,
                    recommendation = uiState.dailyRecommendation
                )
            }
            
            // Featured Workout
            item {
                WorkoutCard(
                    title = uiState.featuredWorkout?.title ?: "Gentle Yoga Flow",
                    duration = uiState.featuredWorkout?.duration ?: 15,
                    intensity = uiState.featuredWorkout?.intensity ?: "Low",
                    onClick = onNavigateToWorkouts
                )
            }
            
            // Quick Actions
            item {
                QuickActionsRow(
                    onTrackPeriod = { /* Track today */ },
                    onViewCalendar = onNavigateToCalendar,
                    onViewWorkouts = onNavigateToWorkouts
                )
            }
        }
    }
}

@Composable
fun WelcomeCard(
    name: String,
    cycleDay: Int,
    nextPrediction: Date?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PinkPrimary, SoftPurple)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hello, $name! 💕",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Day $cycleDay of your cycle",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = nextPrediction?.let {
                        "Next period expected: ${formatDate(it)}"
                    } ?: "Track your first period to get predictions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun QuickStatsRow(
    cycleLength: Int,
    periodLength: Int,
    daysUntilNext: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Cycle",
            value = "$cycleLength",
            subtitle = "days",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Period",
            value = "$periodLength",
            subtitle = "days",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Next in",
            value = "$daysUntilNext",
            subtitle = "days",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CreamWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MutedText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = PinkPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MutedText
            )
        }
    }
}

@Composable
fun DailyRecommendationCard(
    phase: String,
    recommendation: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Lavender.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🌸 $phase Phase",
                style = MaterialTheme.typography.titleMedium,
                color = SoftPurple
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = Charcoal
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    onTrackPeriod: () -> Unit,
    onViewCalendar: () -> Unit,
    onViewWorkouts: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButton(
            icon = Icons.Default.Add,
            label = "Track",
            onClick = onTrackPeriod,
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            icon = Icons.Default.DateRange,
            label = "Calendar",
            onClick = onViewCalendar,
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            icon = Icons.Default.FitnessCenter,
            label = "Workouts",
            onClick = onViewWorkouts,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PinkPrimary
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

fun formatDate(date: Date): String {
    // Implementation for date formatting
    return "Mar 15, 2024"
}