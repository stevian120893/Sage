package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.SubscriptionType
import com.mib.feature_home.repository.HomeWithAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSubscriptionTypeUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<List<SubscriptionType>?, String?> = withContext(Dispatchers.IO) {
        return@withContext homeWithAuthRepository.getSubscriptionTypes()
    }
}