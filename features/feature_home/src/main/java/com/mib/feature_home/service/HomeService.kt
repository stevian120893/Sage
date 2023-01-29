package com.mib.feature_home.service

import com.mib.feature_home.dto.request.VerificationCodeRequest
import com.mib.feature_home.dto.response.BankResponse
import com.mib.feature_home.dto.response.HomeResponse
import com.mib.feature_home.dto.response.LocationResponse
import com.mib.feature_home.dto.response.VerificationCodeResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HomeService {
    @GET("/jokes/random")
    suspend fun get(): NetworkResponse<HomeResponse>

    @Multipart
    @POST("/auth/register")
    suspend fun register(
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("bank_code") bankCode: RequestBody?,
        @Part("bank_account_number") bankAccountNumber: RequestBody?,
        @Part("code") code: RequestBody?,
        @Part profilePicture: MultipartBody.Part?,
        @Part ktpPicture: MultipartBody.Part?,
        @Part ktpSelfiePicture: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<TokenResponse>>

    @POST("/auth/send-code")
    suspend fun sendCode(
        @Body body: VerificationCodeRequest
    ): NetworkResponse<ApiResponse<Void>>

    @GET("/public/cities")
    suspend fun getLocations(): NetworkResponse<ApiResponse<List<LocationResponse>>>

    @GET("/public/banks")
    suspend fun getBanks(): NetworkResponse<ApiResponse<List<BankResponse>>>
}
