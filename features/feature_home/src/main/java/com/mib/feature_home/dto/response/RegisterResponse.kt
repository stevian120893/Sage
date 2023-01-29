package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class RegisterResponse (
    @SerializedName("access_token")
    val accessToken: String?,
)
