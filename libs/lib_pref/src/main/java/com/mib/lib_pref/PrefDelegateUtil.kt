package com.mib.lib_pref

fun <T> safeRead(defaultValue: T, valueReader: () -> T) = try {
    valueReader()
} catch (e: Exception) {
    defaultValue
}
