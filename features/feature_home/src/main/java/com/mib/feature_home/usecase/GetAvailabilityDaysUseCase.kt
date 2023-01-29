package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.AvailabilityDay
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetAvailabilityDaysUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<List<AvailabilityDay>?, String?> {
        return homeWithAuthRepository.getAvailabilityDays()
    }
}