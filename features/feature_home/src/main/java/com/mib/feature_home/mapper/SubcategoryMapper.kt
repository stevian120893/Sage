package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.dto.response.SubcategoryResponse

fun SubcategoryResponse.toDomainModel(): Subcategory {
    return Subcategory(
        subcategoryId = this.subcategoryId.orEmpty(),
        subcategoryName = this.subcategoryName.orEmpty()
    )
}