package com.mib.feature_home.usecase.auth

import com.mib.feature_home.repository.HomeRepository

class SendCodeUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(
        email: String
    ): Pair<Void?, String?> {
        return homeRepository.sendCode(email)
    }
}