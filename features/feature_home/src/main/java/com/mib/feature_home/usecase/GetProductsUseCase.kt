package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetProductsUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(categoryId: String, subcategoryId: String, cursor: String? = null): Pair<ProductsItemPaging, String?> {
        return homeWithAuthRepository.getProducts(categoryId, subcategoryId, cursor)
    }
}