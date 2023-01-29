package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository
import okhttp3.MultipartBody

class AddProductUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        subcategoryCode: String?,
        name: String,
        description: String,
        price: String,
        yearExperience: String,
        image: MultipartBody.Part?,
        productCode: String?,
        action: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.addProduct(
            subcategoryCode,
            name,
            description,
            price,
            yearExperience,
            image,
            productCode,
            action
        )
    }
}