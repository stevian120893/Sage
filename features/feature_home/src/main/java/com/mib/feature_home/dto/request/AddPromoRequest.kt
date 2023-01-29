package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.Date

class AddPromoRequest(
    @SerializedName("promo_title")
    val promoTitle: String,
    @SerializedName("promo_discount_amount")
    val promoDiscountAmount: BigDecimal,
    @SerializedName("minimum_transaction_amount")
    val minimumTransactionAmount: BigDecimal,
    @SerializedName("promo_quota")
    val promoQuota: Int,
    @SerializedName("promo_expired_date")
    val promoExpiredDate: Date,
    @SerializedName("promo_image")
    val promoImage: String
)
