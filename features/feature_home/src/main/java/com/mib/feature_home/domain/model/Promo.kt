package com.mib.feature_home.domain.model

import java.math.BigDecimal

class Promo(
    val promoCode: String,
    val promoTitle: String,
    val promoDescription: String,
    val type: Int,
    val promoDiscountAmount: BigDecimal,
    val minimumTransactionAmount: BigDecimal,
    val maximumDiscount: BigDecimal,
    val promoInputCode: String,
    val promoQuota: Int,
    val promoStartDate: String,
    val promoExpiredDate: String,
    val isTimeLimited: Boolean,
    val promoImageUrl: String,
    val status: String
)