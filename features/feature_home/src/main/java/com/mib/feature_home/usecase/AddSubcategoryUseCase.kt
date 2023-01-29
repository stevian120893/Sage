package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class AddSubcategoryUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        categoryId: String?,
        subcategoryName: String,
        subcategoryId: String?,
        action: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.addSubcategory(
            categoryId, subcategoryName, subcategoryId, action
        )
    }
}