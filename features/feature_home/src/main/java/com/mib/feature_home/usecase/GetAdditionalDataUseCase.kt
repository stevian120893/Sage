package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.AdditionalData
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetAdditionalDataUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<AdditionalData?, String?> {
        return homeWithAuthRepository.getAdditionalData()
    }
}