package com.mib.lib_pref

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty

fun SharedPreferences.string(
    key: String? = null,
    defaultValue: String = "",
    commit: Boolean = false
): ReadWriteProperty<Any, String> = StringPrefDelegates(this, key, defaultValue, commit)

fun SharedPreferences.long(
    key: String? = null,
    defaultValue: Long = 0L,
    commit: Boolean = false
): ReadWriteProperty<Any, Long> = LongPrefDelegates(this, key, defaultValue, commit)

fun SharedPreferences.int(
    key: String? = null,
    defaultValue: Int = 0,
    commit: Boolean = false
): ReadWriteProperty<Any, Int> = IntPrefDelegates(this, key, defaultValue, commit)

fun SharedPreferences.boolean(
    key: String? = null,
    defaultValue: Boolean = false,
    commit: Boolean = false
): ReadWriteProperty<Any, Boolean> = BooleanPrefDelegates(this, key, defaultValue, commit)

