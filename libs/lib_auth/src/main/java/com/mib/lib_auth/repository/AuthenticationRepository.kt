package com.mib.lib_auth.repository

import com.mib.lib_auth.dto.response.TokenResponse

interface AuthenticationRepository {

    suspend fun loginWithEmail(email: String, password: String): Pair<TokenResponse?, String?>

//    suspend fun loginWithPhone(phone: String, otpId: Long, otpCode: String): Pair<TokenResponse?, String?>
}