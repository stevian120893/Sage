package com.mib.lib_pref

import android.content.SharedPreferences
import com.mib.lib_pref.file.AppBoundPref

class DevicePref(
    appBoundPref: () -> SharedPreferences,
) {
    private val _appBoundPref by lazy(appBoundPref)

    var deviceUUID by _appBoundPref.string(key = DEVICE_UUID)

    companion object {
        private const val DEVICE_UUID = "device_uuid"
        private val _INSTANCE by lazy (LazyThreadSafetyMode.SYNCHRONIZED) {
            DevicePref(AppBoundPref::create)
        }

        fun get(): DevicePref {
            return _INSTANCE
        }
    }
}