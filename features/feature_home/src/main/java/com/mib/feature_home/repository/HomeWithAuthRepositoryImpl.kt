package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.AdditionalData
import com.mib.feature_home.domain.model.AdminBank
import com.mib.feature_home.domain.model.AvailabilityDay
import com.mib.feature_home.domain.model.BuySubscription
import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.domain.model.PromosItemPaging
import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.domain.model.SubscriptionOrdersItemPaging
import com.mib.feature_home.domain.model.SubscriptionType
import com.mib.feature_home.domain.model.UserSubscription
import com.mib.feature_home.dto.request.AddCategoryRequest
import com.mib.feature_home.dto.request.AddSubcategoryRequest
import com.mib.feature_home.dto.request.AvailabilityDayRequest
import com.mib.feature_home.mapper.toDomainModel
import com.mib.feature_home.service.HomeAuthenticatedService
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_ADD
import com.mib.feature_home.utils.AppUtils
import com.mib.lib_api.dto.NetworkResponse
import okhttp3.MultipartBody

class HomeWithAuthRepositoryImpl(
    service: () -> HomeAuthenticatedService
): HomeWithAuthRepository {
    private val service by lazy(service)

    override suspend fun getProfile(): Pair<Profile?, String?> {
        val result = service.getProfile()
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun saveProfile(
        name: String,
        location: String,
        bankCode: String,
        bankAccountNumber: String,
        isAcceptCash: Boolean,
        isAcceptBankTransfer: Boolean,
        profilePicture: MultipartBody.Part?
    ): Pair<Profile?, String?> {
        val result = service.saveProfile(
            AppUtils.createRequestBody(name),
            AppUtils.createRequestBody(location),
            AppUtils.createRequestBody(bankCode),
            AppUtils.createRequestBody(bankAccountNumber),
            AppUtils.createRequestBody(if(isAcceptCash) "1" else "0"),
            AppUtils.createRequestBody(if(isAcceptBankTransfer) "1" else "0"),
            profilePicture
        )
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getCategories(cursor: String?): Pair<CategoriesItemPaging, String?> {
        val result = service.getCategories(cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                CategoriesItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                CategoriesItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun addCategory(
        categoryCode: String?,
        categoryName: String,
        action: String
    ): Pair<Void?, String?> {
        val addCategoryRequest = AddCategoryRequest(
            categoryCode = categoryCode,
            categoryName = categoryName
        )

        val result = if(action == ACTION_ADD) {
            service.addCategory(addCategoryRequest)
        } else {
            service.editCategory(addCategoryRequest)
        }

        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getSubcategories(categoryId: String, cursor: String?): Pair<SubcategoriesItemPaging, String?> {
        val result = service.getSubcategories(categoryId, cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                SubcategoriesItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                SubcategoriesItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun addSubcategory(
        categoryId: String?,
        subcategoryName: String,
        subcategoryId: String?,
        action: String
    ): Pair<Void?, String?> {
        val addSubcategoryRequest = AddSubcategoryRequest(
            categoryId = categoryId,
            subcategoryName = subcategoryName,
            subcategoryCode = subcategoryId
        )

        val result = if(action == ACTION_ADD) {
            service.addSubcategory(addSubcategoryRequest)
        } else {
            service.editSubcategory(addSubcategoryRequest)
        }

        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getAvailabilityDays(): Pair<List<AvailabilityDay>?, String?> {
        val result = service.getAvailabilityDays()
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                items to null
            }
            else -> {
                emptyList<AvailabilityDay>() to result.getErrorMessage()
            }
        }
    }

    override suspend fun setAvailabilityDays(
        availabilityDays: List<AvailabilityDay>
    ): Pair<List<AvailabilityDay>?, String?> {
        val availabilityDayRequest = availabilityDays.map {
            AvailabilityDayRequest(it.dayName, it.openHour, it.closedHour, it.isOpen)
        }

        val result = service.setAvailabilityDays(availabilityDayRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                items to null
            }
            else -> {
                emptyList<AvailabilityDay>() to result.getErrorMessage()
            }
        }
    }

    override suspend fun getProducts(categoryId: String, subcategoryId: String, cursor: String?): Pair<ProductsItemPaging, String?> {
        val result = service.getProducts(categoryId, subcategoryId, cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                ProductsItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                ProductsItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun addProduct(
        subcategoryCode: String?,
        name: String,
        description: String,
        price: String,
        yearExperience: String,
        image: MultipartBody.Part?,
        productCode: String?,
        action: String
    ): Pair<Void?, String?> {
        val result = if(action == ACTION_ADD) {
            service.addProduct(
                subcategoryCode = AppUtils.createRequestBody(subcategoryCode),
                name = AppUtils.createRequestBody(name),
                description = AppUtils.createRequestBody(description),
                price = AppUtils.createRequestBody(price),
                yearExperience = AppUtils.createRequestBody(yearExperience),
                image = image,
            )
        } else {
            service.editProduct(
                productCode = AppUtils.createRequestBody(productCode),
                name = AppUtils.createRequestBody(name),
                description = AppUtils.createRequestBody(description),
                price = AppUtils.createRequestBody(price),
                yearExperience = AppUtils.createRequestBody(yearExperience),
                image = image
            )
        }

        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getSubscriptionTypes(): Pair<List<SubscriptionType>?, String?> {
        val result = service.getSubscriptionTypes()
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                items to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getUserSubscription(): Pair<UserSubscription?, String?> {
        val result = service.getUserSubscription()
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()

                if(item?.code.isNullOrBlank()) null to result.value.message else item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun buySubscription(code: String, image: MultipartBody.Part?, referralCode: String?): Pair<BuySubscription?, String?> {
        val result = service.buySubscription(
            code = AppUtils.createRequestBody(code),
            referralCode = AppUtils.createRequestBody(referralCode),
            image = image
        )
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getAdminBank(): Pair<AdminBank?, String?> {
        val result = service.getAdminBank()
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                items[0] to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getSubscriptionOrders(): Pair<SubscriptionOrdersItemPaging?, String?> {
        val result = service.getSubscriptionOrders()
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                SubscriptionOrdersItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                SubscriptionOrdersItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun getPromos(cursor: String?): Pair<PromosItemPaging, String?> {
        val result = service.getPromos(cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                PromosItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                PromosItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun addPromo(
        promoTitle: String,
        promoDescription: String,
        promoDiscountAmount: String,
        minimumTransactionAmount: String,
        promoInputCode: String,
        promoQuota: String,
        promoStartDate: String,
        promoExpiredDate: String,
        promoStatus: String?,
        promoImage: MultipartBody.Part?,
        promoCode: String?,
        action: String
    ): Pair<Void?, String?> {
        val result = if(action == ACTION_ADD) {
            service.addPromo(
                name = AppUtils.createRequestBody(promoTitle),
                description = AppUtils.createRequestBody(promoDescription),
                inputCode = AppUtils.createRequestBody(promoInputCode),
                quota = AppUtils.createRequestBody(promoQuota),
                type = AppUtils.createRequestBody("0"),
                discount = AppUtils.createRequestBody(promoDiscountAmount),
                minimumAmount = AppUtils.createRequestBody(minimumTransactionAmount),
                maximumDiscount = AppUtils.createRequestBody(""),
                startDate = AppUtils.createRequestBody(promoStartDate),
                endDate = AppUtils.createRequestBody(promoExpiredDate),
                isTimeLimited = AppUtils.createRequestBody("1"),
                image = promoImage
            )
        } else {
            service.editPromo(
                promoCode = AppUtils.createRequestBody(promoCode),
                name = AppUtils.createRequestBody(promoTitle),
                description = AppUtils.createRequestBody(promoDescription),
                inputCode = AppUtils.createRequestBody(promoInputCode),
                quota = AppUtils.createRequestBody(promoQuota),
                type = AppUtils.createRequestBody("0"),
                discount = AppUtils.createRequestBody(promoDiscountAmount),
                minimumAmount = AppUtils.createRequestBody(minimumTransactionAmount),
                maximumDiscount = AppUtils.createRequestBody(""),
                startDate = AppUtils.createRequestBody(promoStartDate),
                endDate = AppUtils.createRequestBody(promoExpiredDate),
                isTimeLimited = AppUtils.createRequestBody("1"),
                status = AppUtils.createRequestBody(promoStatus),
                image = promoImage
            )
        }

        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getAdditionalData(): Pair<AdditionalData?, String?> {
        val result = service.getAdditionalData()
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun addAdditionalData(
        simImage: MultipartBody.Part?,
        stnkImage: MultipartBody.Part?,
        skckImage: MultipartBody.Part?,
    ): Pair<Void?, String?> {
        val result = service.addAdditionalData(
            simImage = simImage,
            stnkImage = stnkImage,
            skckImage = skckImage
        )

        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }
}