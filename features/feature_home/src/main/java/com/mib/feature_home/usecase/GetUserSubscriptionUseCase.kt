package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.UserSubscription
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetUserSubscriptionUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<UserSubscription?, String?> {
        return homeWithAuthRepository.getUserSubscription()
    }
}