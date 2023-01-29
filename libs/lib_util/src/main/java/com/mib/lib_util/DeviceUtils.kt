package com.mib.lib_util

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.mib.lib_pref.DevicePref
import java.util.UUID

object DeviceUtils {
    /**
     * TODO: comply with user data tracking recommendation?
     * https://developer.android.com/training/articles/user-data-ids#best-practices-android-identifiers
     */
    @SuppressLint("HardwareIds")
    fun getDeviceUniqueId(): String {
        val androidId: String? = Settings.Secure.getString(
            Utils.context?.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return androidId?.takeIf { !androidId.isNullOrBlank() } ?: let {
            val devicePrefInstance = DevicePref.get()
            if(devicePrefInstance.deviceUUID.isBlank()) {
                val randomUUID = UUID.randomUUID().toString()
                devicePrefInstance.deviceUUID = randomUUID
                randomUUID
            } else {
                devicePrefInstance.deviceUUID
            }
        }
    }

    fun getUserAgent(): String {
        val allowedUriCharsSpace = " @#&=*+-_.,:;!?()/~'%"
        val defaultUserAgent = System.getProperty("http.agent")
        val appVersionCode = ""
        val userAgent = "$defaultUserAgent $appVersionCode SageAndroid".trim()
        return Uri.encode(userAgent, allowedUriCharsSpace)
    }

    fun isPackageInstalled(packageNames: List<String>, packageManager: PackageManager): Boolean {
        return try {
            for(packageName : String in packageNames) {
                packageManager.getPackageInfo(packageName, 0)
            }
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
