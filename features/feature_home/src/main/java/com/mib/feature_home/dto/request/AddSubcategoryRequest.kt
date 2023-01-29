package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class AddSubcategoryRequest(
    @SerializedName("category_code")
    val categoryId: String?,
    @SerializedName("name")
    val subcategoryName: String,
    @SerializedName("code")
    val subcategoryCode: String?
)
