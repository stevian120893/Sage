package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetOrderDetailUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        orderId: String
    ): Pair<OrderDetail?, String?> {
        return homeWithAuthRepository.getOrderDetail(orderId)
    }
}