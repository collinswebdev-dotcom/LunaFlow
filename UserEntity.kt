package com.lunaflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String = "default_user",
    val name: String,
    val email: String?,
    val age: Int,
    val averageCycleLength: Int = 28,
    val averagePeriodLength: Int = 5,
    val lastPeriodDate: Date?,
    val notificationEnabled: Boolean = true,
    val voiceReminderEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val profilePicture: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)