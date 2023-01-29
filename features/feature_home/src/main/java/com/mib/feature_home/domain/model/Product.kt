package com.mib.feature_home.domain.model

import java.math.BigDecimal

class Product(
    val categoryCode: String,
    val categoryName: String,
    val subcategoryCode: String,
    val subcategoryName: String,
    val productCode: String,
    val productName: String,
    val productDescription: String,
    val productImageUrl: String,
    val price: BigDecimal,
    val yearsOfExperience: Int,
    val status: String
)