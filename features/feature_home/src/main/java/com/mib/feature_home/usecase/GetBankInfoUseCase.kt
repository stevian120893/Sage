package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.AdminBank
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetBankInfoUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(): Pair<AdminBank?, String?> {
        return homeWithAuthRepository.getAdminBank()
    }
}