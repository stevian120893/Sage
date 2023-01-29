package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class AddCategoryUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        categoryCode: String?,
        categoryName: String,
        action: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.addCategory(
            categoryCode,
            categoryName,
            action
        )
    }

    companion object {
        const val ACTION_ADD = "add"
        const val ACTION_EDIT = "edit"
    }
}