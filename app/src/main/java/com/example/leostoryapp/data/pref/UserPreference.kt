package com.example.leostoryapp.data.pref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class UserPreference(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_TOKEN = "token"
    }

    fun saveSession(token: String) {
        if (token.trim().isNotEmpty()) {
            sharedPreferences.edit().apply {
                putBoolean(KEY_IS_LOGGED_IN, true)
                putString(KEY_TOKEN, token.trim())
                apply()
            }
            Log.d("UserPreference", "Session saved with token: $token")
        } else {
            throw IllegalArgumentException("Token tidak valid")
        }
    }

    fun isLoggedIn(): Boolean {
        val loggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        Log.d("UserPreference", "Is logged in: $loggedIn")
        return loggedIn
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString(KEY_TOKEN, null)
        Log.d("UserPreference", "Retrieved token: $token")
        return token
    }

    fun clearSession() {
        sharedPreferences.edit().apply {
            remove(KEY_TOKEN)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
        Log.d("UserPreference", "Session cleared")
    }
}
