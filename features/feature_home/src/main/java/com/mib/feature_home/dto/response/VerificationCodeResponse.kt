package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.util.Date

class VerificationCodeResponse (
    @SerializedName("code")
    val code: String?
)
