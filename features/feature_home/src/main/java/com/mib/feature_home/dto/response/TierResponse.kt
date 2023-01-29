package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class TierResponse (
    @SerializedName("current_tier")
    val currentTier: String?,
    @SerializedName("image_tier")
    val imageTier: String?,
    @SerializedName("referral_amount")
    val referralAmount: String?,
    @SerializedName("next_tier")
    val nextTier: String?
)
