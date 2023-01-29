package com.mib.lib_auth.dto.request

import com.google.gson.annotations.SerializedName

class UserOtp(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("otp_id")
    val otpId: Long,
    @SerializedName("otp_code")
    val otpCode: String
)