package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetProfileUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<Profile?, String?> {
        return homeWithAuthRepository.getProfile()
    }
}