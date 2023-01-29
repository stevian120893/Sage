package com.mib.lib_auth.repository

import com.mib.lib_auth.dto.response.RefreshResponse
import com.mib.lib_auth.dto.response.TokenResponse
import com.mib.lib_auth.storage.SecureSessionPref
import com.mib.lib_pref.SessionPref
import com.mib.lib_pref.file.UserBoundPref

class SessionRepositoryImpl(
    private val secureSessionPref: SecureSessionPref,
    private val sessionPref: SessionPref
) : SessionRepository {

    override fun saveLoginInfo(response: TokenResponse) {
        secureSessionPref.accessToken = response.accessToken ?: ""
//        secureSessionPref.refreshToken = response.refreshToken ?: ""
//        sessionPref.userId = response.userId ?: 0L
//        if (response.requiresSignup == true) {
//            sessionPref.sessionType = SessionPref.LIMITED
//        } else {
//            sessionPref.sessionType = SessionPref.USER
//        }
    }

    override fun saveRefreshInfo(response: RefreshResponse) {
        secureSessionPref.accessToken = response.accessToken ?: ""
        secureSessionPref.refreshToken = response.refreshToken ?: ""
    }

    override fun getAccessToken(): String? {
        return secureSessionPref.accessToken.takeIf { it.isNotEmpty() }
    }

    override fun getRefreshToken(): String? {
        return secureSessionPref.refreshToken.takeIf { it.isNotEmpty() }
    }

    override fun getSessionType(): String? {
        return sessionPref.sessionType.takeIf { it.isNotEmpty() }
    }

    override fun clearLocalSession() {
        UserBoundPref.clear()
        secureSessionPref.clear()
    }
}