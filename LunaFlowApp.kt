package com.lunaflow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LunaFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide components
    }
}