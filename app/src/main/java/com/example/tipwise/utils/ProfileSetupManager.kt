package com.example.tipwise.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileSetupManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences("profile_setup", Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sharedPreferences.edit().putString("user_id", userId).apply()
        }
    }

    suspend fun getUserId(): String? {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString("user_id", null)
        }
    }
}