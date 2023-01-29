package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.SubscriptionOrdersItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetSubscriptionOrdersUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<SubscriptionOrdersItemPaging?, String?> {
        return homeWithAuthRepository.getSubscriptionOrders()
    }
}