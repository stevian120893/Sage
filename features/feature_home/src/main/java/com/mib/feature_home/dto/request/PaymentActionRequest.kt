package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class PaymentActionRequest(
    @SerializedName("code")
    val code: String
)
