package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class SubscriptionOrderResponse (
    @SerializedName("code")
    val code: String?,
    @SerializedName("start_date")
    val startDate: Long?,
    @SerializedName("end_date")
    val endDate: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("month")
    val month: Int?,
    @SerializedName("request_date")
    val requestDate: Long?,
    @SerializedName("status")
    val status: String?
)
