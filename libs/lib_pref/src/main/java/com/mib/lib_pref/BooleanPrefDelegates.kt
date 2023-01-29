package com.mib.lib_pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.mib.lib_pref.safeRead
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BooleanPrefDelegates(
    private val pref: SharedPreferences,
    private val key: String?,
    private val defaultValue: Boolean,
    private val commit: Boolean
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>) = safeRead(defaultValue) {
        pref.getBoolean(key ?: property.name, defaultValue)
    }


    @SuppressLint("ApplySharedPref")
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        val editor = pref.edit().putBoolean(key ?: property.name, value)
        if (commit) editor.commit()
        else editor.apply()
    }
}
