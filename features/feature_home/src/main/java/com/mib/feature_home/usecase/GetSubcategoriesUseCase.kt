package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetSubcategoriesUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(categoryId: String, cursor: String? = null): Pair<SubcategoriesItemPaging, String?> {
        return homeWithAuthRepository.getSubcategories(categoryId, cursor)
    }
}