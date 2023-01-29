package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.Bank
import com.mib.feature_home.repository.HomeRepository

class GetBanksUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Pair<List<Bank>?, String?> {
        return homeRepository.getBanks()
    }
}