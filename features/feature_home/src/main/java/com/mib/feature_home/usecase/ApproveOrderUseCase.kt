package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class ApproveOrderUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        code: String,
        paymentMethod: String,
        price: String,
        bookingDate: String,
        note: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.approveOrder(
            code,
            paymentMethod,
            price,
            bookingDate,
            note,
        )
    }
}