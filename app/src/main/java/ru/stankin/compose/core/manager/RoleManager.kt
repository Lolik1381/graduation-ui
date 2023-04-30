package ru.stankin.compose.core.manager

import android.content.Context
import android.content.SharedPreferences
import ru.stankin.compose.R

object RoleManager {
    private const val KEY = "ROLE"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        remove()
    }

    fun put(roles: List<String>) {
        val editor = prefs.edit()

        editor.putStringSet(KEY, roles.toSet())
        editor.apply()
    }

    fun get(): List<String> {
        return prefs.getStringSet(KEY, null)?.toList() ?: emptyList()
    }

    fun remove() {
        val editor = prefs.edit()

        editor.remove(KEY)
        editor.apply()
    }
}