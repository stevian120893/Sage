package com.mib.lib_auth.dto.response

import com.google.gson.annotations.SerializedName

class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String?
//    @SerializedName("refresh_token")
//    val refreshToken: String?,
//    @SerializedName("requires_signup")
//    val requiresSignup: Boolean?,
//    @SerializedName("user_id")
//    val userId: Long? // Returns 0 if user fresh sign up
)