package com.mib.lib_user.service

import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_user.dto.response.UserInfoResponse
import retrofit2.http.GET

interface UserAuthenticatedService {

    @GET("/auth/user_info")
    suspend fun getUserInfo(): NetworkResponse<ApiResponse<UserInfoResponse>>

//    @PATCH("/auth/user_info")
//    suspend fun updateUserInfo(
//        @Body body: UpdateUserInfoRequest
//    ): NetworkResponse<ApiResponse<Unit>>
//
//    @GET("/auth/user_consent")
//    suspend fun getLastUserConsent(): NetworkResponse<ApiResponse<UserConsentResponse>>
//
//    @POST("/auth/user_consent")
//    suspend fun updateLastUserConsent(): NetworkResponse<ApiResponse<UserConsentResponse>>
}