package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class PromoResponse (
    @SerializedName("code")
    val promoCode: String?,
    @SerializedName("name")
    val promoTitle: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("type")
    val type: Int?,
    @SerializedName("discount")
    val promoDiscountAmount: BigDecimal?,
    @SerializedName("minimum_amount")
    val minimumTransactionAmount: BigDecimal?,
    @SerializedName("maximum_discount")
    val maximumDiscount: BigDecimal?,
    @SerializedName("input_code")
    val promoInputCode: String?,
    @SerializedName("quota")
    val promoQuota: Int?,
    @SerializedName("start_date")
    val promoStartDate: Long?,
    @SerializedName("end_date")
    val promoExpiredDate: Long?,
    @SerializedName("is_time_limited")
    val isTimeLimited: Boolean?,
    @SerializedName("image_path")
    val promoImageUrl: String?,
    @SerializedName("status")
    val status: String?
)
