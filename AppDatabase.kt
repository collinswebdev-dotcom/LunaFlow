package com.lunaflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lunaflow.data.local.dao.CycleDao
import com.lunaflow.data.local.dao.UserDao
import com.lunaflow.data.local.dao.WorkoutDao
import com.lunaflow.data.local.entity.CycleEntity
import com.lunaflow.data.local.entity.UserEntity
import com.lunaflow.data.local.entity.WorkoutEntity

@Database(
    entities = [
        UserEntity::class,
        CycleEntity::class,
        WorkoutEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cycleDao(): CycleDao
    abstract fun workoutDao(): WorkoutDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lunaflow_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}