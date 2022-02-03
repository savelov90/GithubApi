package com.example.githubapi.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext
    private val preference: SharedPreferences =
        appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveRecyclerPosition(position: Int) {
        preference.edit { putInt(KEY_RECYCLER_IND, position) }
    }

    fun getRecyclerPosition(): Int {
        return preference.getInt(KEY_RECYCLER_IND, RECYCLER_IND)
    }

    fun savePaginationID(id: Int) {
        preference.edit { putInt(KEY_PAGINATION_ID, id) }
    }

    fun getPaginationID(): Int {
        return preference.getInt(KEY_PAGINATION_ID, PAGINATION_ID)
    }

    fun saveLaunch(launch: Boolean) {
        preference.edit { putBoolean(KEY_LAUNCH, launch) }
    }

    fun getLaunch(): Boolean {
        return preference.getBoolean(KEY_LAUNCH, LAUNCH)
    }

    companion object {
        private const val KEY_RECYCLER_IND = "KEY_POSITION"
        private const val RECYCLER_IND = 1
        private const val KEY_PAGINATION_ID = "KEY_ID"
        private const val PAGINATION_ID = 0
        private const val KEY_LAUNCH = "LAUNCHER"
        private const val LAUNCH = true

    }
}