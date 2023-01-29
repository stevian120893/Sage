package com.mib.lib_auth.usecase

import com.mib.lib_auth.dto.response.TokenResponse
import com.mib.lib_auth.repository.AuthenticationRepository
import com.mib.lib_auth.repository.SessionRepository

class LoginUseCase(
    private val authRepo: AuthenticationRepository,
    private val sessionRepo: SessionRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Pair<TokenResponse?, String?> {
        val response = authRepo.loginWithEmail(email, password)
        // save the token in the session
        response.first?.let {
            sessionRepo.saveLoginInfo(it)
        }
        return response
    }
}
