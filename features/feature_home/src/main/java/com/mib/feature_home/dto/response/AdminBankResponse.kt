package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class AdminBankResponse (
    @SerializedName("bank_code")
    val bankCode: String?,
    @SerializedName("bank_name")
    val bankName: String?,
    @SerializedName("account_number")
    val accountNumber: String?
)
