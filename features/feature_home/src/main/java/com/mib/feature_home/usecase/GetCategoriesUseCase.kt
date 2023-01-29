package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetCategoriesUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(cursor: String? = null): Pair<CategoriesItemPaging, String?> {
        return homeWithAuthRepository.getCategories(cursor)
    }
}