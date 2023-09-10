package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class CancelDoneOrderRequest(
    @SerializedName("code")
    val code: String
)
