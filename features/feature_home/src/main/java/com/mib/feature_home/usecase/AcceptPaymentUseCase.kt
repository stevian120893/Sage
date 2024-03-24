package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class AcceptPaymentUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        code: String,
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.acceptPayment(
            code,
        )
    }
}