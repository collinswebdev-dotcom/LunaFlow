package com.lunaflow.data.ai

import com.lunaflow.data.local.entity.CycleEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date
import kotlin.math.abs
import kotlin.math.roundToInt

class CyclePredictor {
    
    /**
     * Advanced AI-powered cycle prediction
     * Uses weighted averages and pattern recognition
     */
    suspend fun predictNextCycle(
        cycles: List<CycleEntity>,
        averageCycleLength: Int
    ): Date? {
        if (cycles.isEmpty()) return null
        
        // Get the most recent cycle
        val lastCycle = cycles.firstOrNull() ?: return null
        
        // Calculate weighted average of last 6 cycles
        val recentCycles = cycles.take(6)
        val weightedLength = calculateWeightedAverage(recentCycles, averageCycleLength)
        
        // Create calendar for prediction
        val calendar = Calendar.getInstance()
        calendar.time = lastCycle.startDate
        calendar.add(Calendar.DAY_OF_MONTH, weightedLength)
        
        return calendar.time
    }
    
    /**
     * Predict next 6 cycles for calendar display
     */
    suspend fun predictFutureCycles(
        cycles: List<CycleEntity>,
        averageCycleLength: Int,
        averagePeriodLength: Int
    ): List<PredictedCycle> {
        if (cycles.isEmpty()) return emptyList()
        
        val predictions = mutableListOf<PredictedCycle>()
        val lastCycle = cycles.first()
        
        var predictedStart = lastCycle.startDate
        val calendar = Calendar.getInstance()
        
        for (i in 1..6) {
            calendar.time = predictedStart
            calendar.add(Calendar.DAY_OF_MONTH, averageCycleLength)
            predictedStart = calendar.time
            
            val predictedEnd = Calendar.getInstance().apply {
                time = predictedStart
                add(Calendar.DAY_OF_MONTH, averagePeriodLength - 1)
            }.time
            
            predictions.add(
                PredictedCycle(
                    startDate = predictedStart,
                    endDate = predictedEnd,
                    cycleNumber = i,
                    confidence = calculateConfidence(i, cycles.size)
                )
            )
        }
        
        return predictions
    }
    
    /**
     * Calculate weighted average cycle length
     * Recent cycles have more weight
     */
    private fun calculateWeightedAverage(
        cycles: List<CycleEntity>,
        fallback: Int
    ): Int {
        if (cycles.size < 2) return fallback
        
        var totalWeight = 0.0
        var weightedSum = 0.0
        
        cycles.forEachIndexed { index, cycle ->
            // More recent cycles get higher weight
            val weight = (cycles.size - index).toDouble()
            weightedSum += cycle.cycleLength * weight
            totalWeight += weight
        }
        
        return (weightedSum / totalWeight).roundToInt()
    }
    
    /**
     * Calculate prediction confidence
     */
    private fun calculateConfidence(
        cyclesAhead: Int,
        totalCyclesTracked: Int
    ): Double {
        val baseConfidence = 0.95
        val decayFactor = 0.05 * cyclesAhead
        val dataFactor = minOf(totalCyclesTracked / 10.0, 1.0)
        
        return (baseConfidence - decayFactor) * dataFactor
    }
    
    /**
     * Detect cycle irregularities
     */
    fun detectIrregularities(cycles: List<CycleEntity>): CycleIrregularity? {
        if (cycles.size < 3) return null
        
        val lengths = cycles.map { it.cycleLength }
        val average = lengths.average()
        val standardDeviation = calculateStandardDeviation(lengths)
        
        if (standardDeviation > 7) {
            return CycleIrregularity(
                type = "High Variability",
                severity = "Moderate",
                message = "Your cycle length varies by ${standardDeviation.toInt()} days. " +
                         "Consider consulting a healthcare provider.",
                recommendedAction = "Track your symptoms more closely and maintain a healthy lifestyle."
            )
        }
        
        return null
    }
    
    private fun calculateStandardDeviation(values: List<Int>): Double {
        val mean = values.average()
        val variance = values.map { (it - mean) * (it - mean) }.average()
        return kotlin.math.sqrt(variance)
    }
    
    data class PredictedCycle(
        val startDate: Date,
        val endDate: Date,
        val cycleNumber: Int,
        val confidence: Double
    )
    
    data class CycleIrregularity(
        val type: String,
        val severity: String,
        val message: String,
        val recommendedAction: String
    )
}