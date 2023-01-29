package com.mib.lib_pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringPrefDelegates(
    private val pref: SharedPreferences,
    private val key: String?,
    private val defaultValue: String,
    private val commit: Boolean
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>) = safeRead(defaultValue) {
        pref.getString(key ?: property.name, null) ?: defaultValue
    }

    @SuppressLint("ApplySharedPref")
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        val editor = pref.edit().putString(key ?: property.name, value)
        if (commit) editor.commit()
        else editor.apply()
    }
}
