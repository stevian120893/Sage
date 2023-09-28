package com.mib.feature_home.dto.response.order_detail

import com.google.gson.annotations.SerializedName

class ProductResponse (
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?
)
