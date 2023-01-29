package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class AddCategoryRequest(
    @SerializedName("code")
    val categoryCode: String?,
    @SerializedName("name")
    val categoryName: String
)
