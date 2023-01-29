package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Pair<Home?, String?> = withContext(Dispatchers.IO) {
        return@withContext homeRepository.get()
    }
}