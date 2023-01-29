package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class SubscriptionTypeResponse (
    @SerializedName("code")
    val subscriptionCode: String?,
    @SerializedName("name")
    val subscriptionName: String?,
    @SerializedName("price")
    val subscriptionPrice: BigDecimal?,
)
