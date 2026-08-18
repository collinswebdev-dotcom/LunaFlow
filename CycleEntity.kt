package com.lunaflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cycles")
data class CycleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startDate: Date,
    val endDate: Date?,
    val cycleLength: Int,
    val periodLength: Int,
    val symptoms: List<String> = emptyList(),
    val mood: String?,
    val flowIntensity: String, // Light, Medium, Heavy
    val notes: String?,
    val timestamp: Long = System.currentTimeMillis()
)