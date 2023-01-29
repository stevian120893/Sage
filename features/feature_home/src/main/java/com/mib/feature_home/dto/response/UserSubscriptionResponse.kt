package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.util.Date

class UserSubscriptionResponse (
    @SerializedName("code")
    val code: String?,
    @SerializedName("start_date")
    val startDate: Long?,
    @SerializedName("end_date")
    val endDate: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("month")
    val month: Int?
)
