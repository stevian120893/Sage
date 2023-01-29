package com.mib.lib_auth.repository

import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.request.LoginRequest
import com.mib.lib_auth.dto.response.TokenResponse
import com.mib.lib_auth.service.AuthenticationService

class AuthenticationRepositoryImpl(
    service: () -> AuthenticationService
) : AuthenticationRepository {

    private val service by lazy(service)

    override suspend fun loginWithEmail(email: String, password: String): Pair<TokenResponse?, String?> {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )
        val result = service.login(loginRequest)
        return when (result) {
            is NetworkResponse.Success -> result.value.data to null
            else -> null to result.getErrorMessage()
        }
    }

//    override suspend fun loginWithPhone(phone: String, otpId: Long, otpCode: String): Pair<TokenResponse?, String?> {
//        val result = service.login(
//            LoginTypes.PHONE_OTP,
//            UserOtp(phone = phone, otpId = otpId, otpCode = otpCode)
//        )
//        return when (result) {
//            is NetworkResponse.Success -> result.value.data to null
//            else -> null to result.getErrorMessage()
//        }
//    }
}