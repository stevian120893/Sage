package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class SubcategoryResponse (
    @SerializedName("category_code")
    val categoryCode: String?,
    @SerializedName("code")
    val subcategoryId: String?,
    @SerializedName("name")
    val subcategoryName: String?,
    @SerializedName("status")
    val status: String?
)
