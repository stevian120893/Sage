package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class VerificationCodeRequest(
    @SerializedName("email")
    val email: String
)
