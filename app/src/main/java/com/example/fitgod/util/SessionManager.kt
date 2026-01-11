package com.example.fitgod.util

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "fitgod_session",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveLogin(userId: Int, username: String) {
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean =
        prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserId(): Int =
        prefs.getInt(KEY_USER_ID, -1)

    fun getUsername(): String? =
        prefs.getString(KEY_USERNAME, null)
}
