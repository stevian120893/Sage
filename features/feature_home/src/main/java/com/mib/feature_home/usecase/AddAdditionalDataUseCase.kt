package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository
import okhttp3.MultipartBody

class AddAdditionalDataUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        simImage: MultipartBody.Part?,
        stnkImage: MultipartBody.Part?,
        skckImage: MultipartBody.Part?
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.addAdditionalData(
            simImage,
            stnkImage,
            skckImage
        )
    }
}