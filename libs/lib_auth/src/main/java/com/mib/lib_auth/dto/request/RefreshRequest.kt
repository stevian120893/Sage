package com.mib.lib_auth.dto.request

import com.google.gson.annotations.SerializedName

class RefreshRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)
