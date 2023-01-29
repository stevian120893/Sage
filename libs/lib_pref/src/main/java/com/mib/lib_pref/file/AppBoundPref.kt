package com.mib.lib_pref.file

import android.content.Context
import android.content.SharedPreferences
import com.mib.lib_pref.init.LibPref.context

/**
 * SharedPreferences that will be cleared when the app in uninstalled or data cleared
 */
object AppBoundPref {

    /**
     * This cannot be changed, for backward compatibility purpose
     */
    private val fileName = "com.mib.sage.AppsPref"

    fun create(): SharedPreferences {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }
}