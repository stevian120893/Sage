package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.Location
import com.mib.feature_home.repository.HomeRepository

class GetLocationsUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Pair<List<Location>?, String?> {
        return homeRepository.getLocations()
    }
}