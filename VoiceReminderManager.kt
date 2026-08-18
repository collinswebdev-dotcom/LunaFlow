package com.lunaflow.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VoiceReminderManager(private val context: Context) : TextToSpeech.OnInitListener {
    
    private var tts: TextToSpeech? = null
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady
    
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking
    
    init {
        tts = TextToSpeech(context, this)
    }
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            tts?.setSpeechRate(0.9f)
            tts?.setPitch(1.1f)
            _isReady.value = true
        }
    }
    
    fun speakReminder(message: String) {
        if (_isReady.value && !_isSpeaking.value) {
            _isSpeaking.value = true
            tts?.speak(
                message,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "reminder_${System.currentTimeMillis()}"
            )
        }
    }
    
    fun speakWelcomeMessage(name: String) {
        val welcomeMessage = "Welcome to LunaFlow, $name! " +
                "I'm here to help you track your cycle and stay healthy. " +
                "Let's begin this beautiful journey together."
        speakReminder(welcomeMessage)
    }
    
    fun speakCyclePrediction(daysUntil: Int) {
        val message = when {
            daysUntil > 1 -> "Your period is expected in $daysUntil days."
            daysUntil == 1 -> "Your period is expected tomorrow. Time to prepare!"
            daysUntil == 0 -> "Your period may start today. Take care of yourself."
            else -> "Your period has started. Remember to track your symptoms."
        }
        speakReminder(message)
    }
    
    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }
    
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        _isReady.value = false
    }
}