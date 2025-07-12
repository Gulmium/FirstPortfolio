package com.example.aluka.data

import android.content.Context
import androidx.core.content.edit

object TokenManager {
    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPref.getString("access", null)
    }

    fun saveToken(context: Context, token: String?) {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPref.edit() { putString("access", token) }
    }

}