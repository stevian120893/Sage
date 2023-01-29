package com.mib.lib_auth

import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.request.RefreshRequest
import com.mib.lib_auth.dto.response.RefreshResponse
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_auth.service.AuthenticationService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AccessTokenAuthenticator(
    private val sessionRepository: SessionRepository,
    private val authenticationService: AuthenticationService,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // We need to have a token in order to refresh it.
        val token = sessionRepository.getAccessToken() ?: return null

        synchronized(this) {
            val newToken = sessionRepository.getAccessToken()

            // Check if the request made was previously made as an authenticated request.
            if (response.request.header("token") != null) {

                // If the token has changed since the request was made, use the new token.
                if (newToken != token) {
                    return response.request
                        .newBuilder()
                        .removeHeader("token")
                        .addHeader("token", "$newToken")
                        .build()
                }

                val refreshToken = sessionRepository.getRefreshToken() ?: return null

                val networkResponse = runBlocking {
                    authenticationService.refresh(RefreshRequest(refreshToken))
                }
                val refreshResponse: RefreshResponse? = if (networkResponse is NetworkResponse.Success) {
                    networkResponse.value.data
                } else null

                if (refreshResponse == null) return null

                sessionRepository.saveRefreshInfo(refreshResponse)

                // Retry the request with the new token.
                return response.request
                    .newBuilder()
                    .removeHeader("token")
                    .addHeader("token", "${refreshResponse.accessToken}")
                    .build()
            }
        }
        return null
    }
}