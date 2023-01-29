package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.repository.HomeWithAuthRepository
import okhttp3.MultipartBody

class SaveProfileUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        name: String,
        location: String,
        bankCode: String,
        bankAccountNumber: String,
        isAcceptCash: Boolean,
        isAcceptBankTransfer: Boolean,
        profileFile: MultipartBody.Part?
    ): Pair<Profile?, String?> {
        return homeWithAuthRepository.saveProfile(
            name,
            location,
            bankCode,
            bankAccountNumber,
            isAcceptCash,
            isAcceptBankTransfer,
            profileFile
        )
    }
}