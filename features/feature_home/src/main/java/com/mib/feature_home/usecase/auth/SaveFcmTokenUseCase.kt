package com.mib.feature_home.usecase.auth

import com.mib.feature_home.repository.HomeWithAuthRepository

class SaveFcmTokenUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        fcmToken: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.saveFcmToken(fcmToken)
    }
}