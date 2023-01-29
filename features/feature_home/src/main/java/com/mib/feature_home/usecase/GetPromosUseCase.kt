package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.PromosItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetPromosUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(cursor: String? = null): Pair<PromosItemPaging, String?> {
        return homeWithAuthRepository.getPromos(cursor)
    }
}