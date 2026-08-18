package com.lunaflow.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lunaflow.domain.model.Workout
import com.lunaflow.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel,
    onWorkoutClick: (Workout) -> Unit
) {
    val workouts by viewModel.workouts.collectAsState()
    val currentPhase by viewModel.currentPhase.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Fitness & Wellness",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PinkPrimary
                    )
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
            // Current Phase Banner
            item {
                PhaseBanner(phase = currentPhase)
            }
            
            // Recommended Workouts
            item {
                Text(
                    text = "Recommended for Your ${currentPhase} Phase",
                    style = MaterialTheme.typography.titleLarge,
                    color = Charcoal,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(workouts) { workout ->
                WorkoutItem(
                    workout = workout,
                    onClick = { onWorkoutClick(workout) }
                )
            }
        }
    }
}

@Composable
fun PhaseBanner(phase: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PinkPrimary, SoftPurple, WarmGold)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "🌸 $phase Phase",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = getPhaseDescription(phase),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun WorkoutItem(
    workout: Workout,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CreamWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Workout Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PinkLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getWorkoutIcon(workout.type),
                    contentDescription = workout.title,
                    tint = PinkPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Charcoal,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${workout.duration} min • ${workout.intensity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Workout",
                tint = MutedText
            )
        }
    }
}

fun getPhaseDescription(phase: String): String {
    return when (phase) {
        "Menstrual" -> "Focus on gentle movement and self-care"
        "Follicular" -> "Energy is rising - great for cardio!"
        "Ovulation" -> "Peak energy - ideal for strength training"
        "Luteal" -> "Moderate intensity - listen to your body"
        else -> "Stay active and healthy"
    }
}

fun getWorkoutIcon(type: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (type.lowercase()) {
        "yoga" -> Icons.Default.SelfImprovement
        "cardio" -> Icons.Default.DirectionsRun
        "strength" -> Icons.Default.FitnessCenter
        "stretching" -> Icons.Default.Accessibility
        else -> Icons.Default.Favorite
    }
}