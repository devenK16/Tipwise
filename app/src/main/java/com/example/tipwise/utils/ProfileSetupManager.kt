package com.example.tipwise.utils

import android.content.Context

class ProfileSetupManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("profile_setup", Context.MODE_PRIVATE)

    fun isProfileSetupCompleted(): Boolean {
        return sharedPreferences.getBoolean("is_profile_setup_completed", false)
    }

    fun setProfileSetupCompleted(completed: Boolean) {
        sharedPreferences.edit().putBoolean("is_profile_setup_completed", completed).apply()
    }
}