package com.lunaflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lunaflow.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CycleCalendarView(
    currentMonth: YearMonth,
    cycleData: Map<LocalDate, CycleDayType>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit
) {
    Column {
        // Month Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { 
                onMonthChanged(currentMonth.minusMonths(1))
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ChevronLeft,
                    contentDescription = "Previous Month",
                    tint = PinkPrimary
                )
            }
            
            Text(
                text = currentMonth.format(
                    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)
                ),
                style = MaterialTheme.typography.titleLarge,
                color = Charcoal,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { 
                onMonthChanged(currentMonth.plusMonths(1))
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ChevronRight,
                    contentDescription = "Next Month",
                    tint = PinkPrimary
                )
            }
        }
        
        // Week Days Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                .forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelLarge,
                        color = MutedText,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
        }
        
        // Calendar Grid
        val days = generateCalendarDays(currentMonth)
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(8.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(days) { date ->
                if (date != null) {
                    DayCell(
                        date = date,
                        cycleType = cycleData[date],
                        isSelected = date == selectedDate,
                        onClick = { onDateSelected(date) }
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
        
        // Legend
        LegendSection()
    }
}

@Composable
fun DayCell(
    date: LocalDate,
    cycleType: CycleDayType?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when (cycleType) {
        CycleDayType.PERIOD -> PeriodDay
        CycleDayType.PREDICTED_PERIOD -> PredictedPeriod
        CycleDayType.FERTILE -> FertileDay.copy(alpha = 0.2f)
        CycleDayType.OVULATION -> OvulationDay.copy(alpha = 0.3f)
        null -> if (isSelected) PinkLight else Color.Transparent
    }
    
    val textColor = when {
        cycleType == CycleDayType.PERIOD -> Color.White
        isSelected -> PinkDark
        date == LocalDate.now() -> PinkPrimary
        else -> Charcoal
    }
    
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, PinkPrimary, CircleShape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            fontWeight = if (date == LocalDate.now() || isSelected) 
                FontWeight.Bold 
            else 
                FontWeight.Normal
        )
    }
}

@Composable
fun LegendSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LegendItem(color = PeriodDay, label = "Period")
        LegendItem(color = PredictedPeriod, label = "Predicted")
        LegendItem(color = FertileDay, label = "Fertile")
        LegendItem(color = OvulationDay, label = "Ovulation")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MutedText
        )
    }
}

private fun generateCalendarDays(yearMonth: YearMonth): List<LocalDate?> {
    val days = mutableListOf<LocalDate?>()
    val firstDay = yearMonth.atDay(1)
    val lastDay = yearMonth.atEndOfMonth()
    
    // Add empty cells for days before the first of month
    val leadingSpaces = firstDay.dayOfWeek.value - 1
    repeat(leadingSpaces) { days.add(null) }
    
    // Add all days of the month
    for (day in 1..lastDay.dayOfMonth) {
        days.add(LocalDate.of(yearMonth.year, yearMonth.month, day))
    }
    
    return days
}

enum class CycleDayType {
    PERIOD,
    PREDICTED_PERIOD,
    FERTILE,
    OVULATION
}