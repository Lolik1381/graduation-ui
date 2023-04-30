package ru.stankin.compose.core.manager

import android.content.Context
import android.content.SharedPreferences
import ru.stankin.compose.R

object JwtTokenManager {
    private const val KEY = "JWT"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        remove()
    }

    fun put(jwt: String) {
        val editor = prefs.edit()

        editor.putString(KEY, "Bearer $jwt")
        editor.apply()
    }

    fun get(): String? {
        return prefs.getString(KEY, null)
    }

    fun remove() {
        val editor = prefs.edit()

        editor.remove(KEY)
        editor.apply()
    }
}