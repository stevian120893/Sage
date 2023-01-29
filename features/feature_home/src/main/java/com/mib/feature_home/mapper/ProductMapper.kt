package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Product
import com.mib.feature_home.dto.response.ProductResponse
import java.math.BigDecimal

fun ProductResponse.toDomainModel(): Product {
    return Product(
        categoryCode = this.categoryCode.orEmpty(),
        categoryName = this.categoryName.orEmpty(),
        subcategoryCode = this.subcategoryCode.orEmpty(),
        subcategoryName = this.subcategoryName.orEmpty(),
        productCode = this.productCode.orEmpty(),
        productName = this.productName.orEmpty(),
        productDescription = this.productDescription.orEmpty(),
        productImageUrl = this.productImageUrl.orEmpty(),
        price = this.price ?: BigDecimal.ZERO,
        yearsOfExperience = this.serviceYearsExperience  ?: 0,
        status = this.status.orEmpty()
    )
}
