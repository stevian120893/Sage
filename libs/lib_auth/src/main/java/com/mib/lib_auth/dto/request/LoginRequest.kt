package com.mib.lib_auth.dto.request

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
