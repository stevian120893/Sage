package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.AvailabilityDay
import com.mib.feature_home.repository.HomeWithAuthRepository

class SetAvailabilityDaysUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        availabilityDays: List<AvailabilityDay>
    ): Pair<List<AvailabilityDay>?, String?> {
        return homeWithAuthRepository.setAvailabilityDays(
            availabilityDays
        )
    }
}