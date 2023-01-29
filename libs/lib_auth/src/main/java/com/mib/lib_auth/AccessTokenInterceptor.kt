package com.mib.lib_auth

import com.mib.lib_auth.repository.SessionRepository
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(
    private val sessionRepository: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionRepository.getAccessToken()

        return if (token == null) {
            chain.proceed(chain.request())
        } else {
            val authenticatedRequest = chain.request()
                .newBuilder()
                .addHeader("token", "$token")
                .build()
            chain.proceed(authenticatedRequest)
        }
    }
}