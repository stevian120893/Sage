package com.mib.feature_home.dto.response.order_detail

import com.google.gson.annotations.SerializedName

class UserOrderResponse (
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?
)
