package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class SetFcmTokenRequest(
    @SerializedName("fcm_token")
    val fcmToken: String
)
