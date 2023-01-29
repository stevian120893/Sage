package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class ProductResponse (
    @SerializedName("category_code")
    val categoryCode: String?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("subcategory_code")
    val subcategoryCode: String?,
    @SerializedName("subcategory_name")
    val subcategoryName: String?,
    @SerializedName("code")
    val productCode: String?,
    @SerializedName("name")
    val productName: String?,
    @SerializedName("description")
    val productDescription: String?,
    @SerializedName("image_path")
    val productImageUrl: String?,
    @SerializedName("price")
    val price: BigDecimal?,
    @SerializedName("service_years_experience")
    val serviceYearsExperience: Int?,
    @SerializedName("status")
    val status: String?,
)
