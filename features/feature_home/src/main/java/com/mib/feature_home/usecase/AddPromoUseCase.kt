package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository
import java.math.BigDecimal
import java.util.Date
import okhttp3.MultipartBody

class AddPromoUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        promoTitle: String,
        promoDescription: String,
        promoDiscountAmount: String,
        minimumTransactionAmount: String,
        promoInputCode: String,
        promoQuota: String,
        promoStartDate: String,
        promoExpiredDate: String,
        promoImage: MultipartBody.Part?,
        promoStatus: String?,
        promoCode: String?,
        action: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.addPromo(
            promoTitle = promoTitle,
            promoDescription = promoDescription,
            promoDiscountAmount = promoDiscountAmount,
            minimumTransactionAmount = minimumTransactionAmount,
            promoInputCode = promoInputCode,
            promoQuota = promoQuota,
            promoStartDate = promoStartDate,
            promoExpiredDate = promoExpiredDate,
            promoImage = promoImage,
            promoStatus = promoStatus,
            promoCode = promoCode,
            action = action
        )
    }
}