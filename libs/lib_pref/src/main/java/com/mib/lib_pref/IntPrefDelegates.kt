package com.mib.lib_pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class IntPrefDelegates(
    private val pref: SharedPreferences,
    private val key: String?,
    private val defaultValue: Int,
    private val commit: Boolean
) : ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>) = safeRead(defaultValue) {
        pref.getInt(key ?: property.name, defaultValue)
    }

    @SuppressLint("ApplySharedPref")
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        val editor = pref.edit().putInt(key ?: property.name, value)
        if (commit) editor.commit()
        else editor.apply()
    }
}
