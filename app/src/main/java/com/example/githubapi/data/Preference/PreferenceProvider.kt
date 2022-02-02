package com.example.newspaper.data.Preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveRecyclerPosition(position: Int) {
        preference.edit { putInt(KEY_RECYCLER_IND, position) }
    }

    fun getRecyclerPosition(): Int {
        return preference.getInt(KEY_RECYCLER_IND, RECYCLER_IND)
    }

    companion object {
        private const val KEY_RECYCLER_IND = "KEY_POSITION"
        private const val RECYCLER_IND = 1
    }
}