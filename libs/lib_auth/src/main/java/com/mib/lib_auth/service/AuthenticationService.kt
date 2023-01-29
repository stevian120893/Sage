package com.mib.lib_auth.service

import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.request.LoginRequest
import com.mib.lib_auth.dto.request.RefreshRequest
import com.mib.lib_auth.dto.response.RefreshResponse
import com.mib.lib_auth.dto.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * This service should be initiated by `Api.service` instead of `Api.serviceWithAuth` to bootstrap
 * the authenticator for `Api.serviceWithAuth`.
 */
interface AuthenticationService {

    @POST("/auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): NetworkResponse<ApiResponse<TokenResponse>>

    @POST("/auth/refresh")
    suspend fun refresh(
        @Body body: RefreshRequest
    ): NetworkResponse<ApiResponse<RefreshResponse>>
}