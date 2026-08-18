package com.lunaflow.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lunaflow.MainActivity
import com.lunaflow.R
import java.util.Calendar
import java.util.Date

class NotificationHelper(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID = "lunaflow_reminders"
        const val CHANNEL_NAME = "Cycle Reminders"
        const val CHANNEL_DESCRIPTION = "Reminders for your menstrual cycle"
        const val NOTIFICATION_ID = 1001
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 250, 250)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    null
                )
            }
            
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showPeriodReminder(daysUntilPeriod: Int, predictedDate: Date) {
        val title = when (daysUntilPeriod) {
            2 -> "🌸 Your Period is Coming Soon!"
            1 -> "💗 Your Period Starts Tomorrow!"
            0 -> "💕 Your Period May Start Today!"
            else -> "🌸 Cycle Update"
        }
        
        val message = when (daysUntilPeriod) {
            2 -> "Your cycle is predicted to start in 2 days. " +
                  "Make sure you're prepared!"
            1 -> "Your period is expected tomorrow. " +
                  "Time to get ready!"
            0 -> "Your period may start today. " +
                  "Remember to track your symptoms!"
            else -> "Check your cycle calendar for updates."
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$message\n\nTap to open LunaFlow and track your cycle.")
            )
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(
                NOTIFICATION_ID,
                notification
            )
        } catch (e: SecurityException) {
            // Handle permission not granted
        }
    }
    
    fun scheduleReminder(predictedDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = predictedDate
        calendar.add(Calendar.DAY_OF_MONTH, -2) // Remind 2 days before
        
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}