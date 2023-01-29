package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.BuySubscription
import com.mib.feature_home.repository.HomeWithAuthRepository
import okhttp3.MultipartBody

class BuySubscriptionUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(code: String, image: MultipartBody.Part?, referralCode: String?): Pair<BuySubscription?, String?> {
        return homeWithAuthRepository.buySubscription(code, image, referralCode)
    }
}