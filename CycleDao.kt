package com.lunaflow.data.local.dao

import androidx.room.*
import com.lunaflow.data.local.entity.CycleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycles ORDER BY startDate DESC")
    fun getAllCycles(): Flow<List<CycleEntity>>
    
    @Query("SELECT * FROM cycles WHERE startDate >= :startDate AND startDate <= :endDate")
    fun getCyclesBetweenDates(startDate: Date, endDate: Date): Flow<List<CycleEntity>>
    
    @Query("SELECT * FROM cycles ORDER BY startDate DESC LIMIT 1")
    fun getLastCycle(): Flow<CycleEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycle(cycle: CycleEntity)
    
    @Update
    suspend fun updateCycle(cycle: CycleEntity)
    
    @Delete
    suspend fun deleteCycle(cycle: CycleEntity)
    
    @Query("DELETE FROM cycles WHERE id = :cycleId")
    suspend fun deleteCycleById(cycleId: Long)
    
    @Query("SELECT * FROM cycles ORDER BY startDate DESC LIMIT :limit")
    fun getRecentCycles(limit: Int): Flow<List<CycleEntity>>
}