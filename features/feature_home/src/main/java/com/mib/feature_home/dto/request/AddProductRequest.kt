package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class AddProductRequest(
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_description")
    val productDescription: String,
    @SerializedName("product_image")
    val productImage: String,
)
