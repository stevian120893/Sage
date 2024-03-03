package com.mib.lib_auth.repository

import com.mib.lib_auth.dto.response.RefreshResponse
import com.mib.lib_auth.dto.response.TokenResponse

interface SessionRepository {

    /**
     * Save login response to local storage. Any important credentials must be stored securely.
     */
    fun saveLoginInfo(response: TokenResponse)

    /**
     * Save refresh response to local storage. Any important credentials must be stored securely.
     */
    fun saveRefreshInfo(response: RefreshResponse)

    /**
     * Get access token from local storage.
     */
    fun getAccessToken(): String?

    /**
     * Get refresh token from local storage.
     */
    fun getRefreshToken(): String?

    /**
     * Get the session type from local storage.
     */
    fun getSessionType(): String?

    /**
     * Delete existing session information, this is called on logout.
     */
    fun clearLocalSession()

    fun saveFcmToken(token: String?)

    fun getFcmToken(): String?
}