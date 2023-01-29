package com.mib.lib_auth.dto.response

import com.google.gson.annotations.SerializedName

class RefreshResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
)
