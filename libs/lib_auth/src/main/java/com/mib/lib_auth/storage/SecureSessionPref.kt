package com.mib.lib_auth.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mib.lib_auth.repository.SessionRepositoryImpl
import com.mib.lib_pref.string

/**
 * This should be accessed only from [SessionRepositoryImpl].
 */
class SecureSessionPref(sharedPreferences: () -> SharedPreferences) {

    private val _pref: SharedPreferences by lazy(sharedPreferences)

    var accessToken: String by _pref.string("access_token", defaultValue = "")
    var refreshToken: String by _pref.string("refresh_token", defaultValue = "")

    fun clear() = _pref.edit().clear().apply()

    companion object {
        private const val fileName = "pref_session"

        private fun createSharedPreference(context: Context): SharedPreferences {
            val masterKeyAlias = generateMasterKey(context)
            return EncryptedSharedPreferences.create(
                context,
                fileName,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        private fun generateMasterKey(context: Context): MasterKey {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }

        fun get(context: Context) = SecureSessionPref { createSharedPreference(context) }
    }
}