package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class CategoryResponse (
    @SerializedName("code")
    val categoryId: String?,
    @SerializedName("name")
    val categoryName: String?,
    @SerializedName("status")
    val status: String?
)
