package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class RegisterRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("bank_code")
    val bankCode: String,
    @SerializedName("bank_account_number")
    val bankAccountNumber: String,
    @SerializedName("code")
    val code: String,
)
