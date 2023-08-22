package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.OrderItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetOrdersUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(cursor: String? = null): Pair<OrderItemPaging?, String?> {
        return homeWithAuthRepository.getOrders(cursor)
    }
}