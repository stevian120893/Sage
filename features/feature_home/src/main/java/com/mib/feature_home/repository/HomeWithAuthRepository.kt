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
import okhttp3.MultipartBody

interface HomeWithAuthRepository {
    suspend fun getProfile(): Pair<Profile?, String?>
    suspend fun saveProfile(
        name: String,
        location: String,
        bankCode: String,
        bankAccountNumber: String,
        isAcceptCash: Boolean,
        isAcceptBankTransfer: Boolean,
        profilePicture: MultipartBody.Part?
    ): Pair<Profile?, String?>

    // category
    suspend fun getCategories(cursor: String?): Pair<CategoriesItemPaging, String?>
    suspend fun addCategory(categoryCode: String?, categoryName: String, action: String): Pair<Void?, String?>

    // subcategory
    suspend fun getSubcategories(categoryId: String, cursor: String?): Pair<SubcategoriesItemPaging, String?>
    suspend fun addSubcategory(categoryId: String?, subcategoryName: String, subcategoryId: String?, action: String): Pair<Void?, String?>

    // availability
    suspend fun getAvailabilityDays(): Pair<List<AvailabilityDay>?, String?>
    suspend fun setAvailabilityDays(availabilityDays: List<AvailabilityDay>): Pair<List<AvailabilityDay>?, String?>

    // product
    suspend fun getProducts(categoryId: String, subcategoryId: String, cursor: String? = null): Pair<ProductsItemPaging, String?>
    suspend fun addProduct(
        subcategoryCode: String?,
        name: String,
        description: String,
        price: String,
        yearExperience: String,
        image: MultipartBody.Part?,
        productCode: String?,
        action: String
    ): Pair<Void?, String?>

    // subscription
    suspend fun getSubscriptionTypes(): Pair<List<SubscriptionType>?, String?>
    suspend fun getUserSubscription(): Pair<UserSubscription?, String?>
    suspend fun buySubscription(code: String, image: MultipartBody.Part?, referralCode: String?): Pair<BuySubscription?, String?>
    suspend fun getAdminBank(): Pair<AdminBank?, String?>
    suspend fun getSubscriptionOrders(): Pair<SubscriptionOrdersItemPaging?, String?>

    // promo
    suspend fun getPromos(cursor: String?): Pair<PromosItemPaging, String?>
    suspend fun addPromo(
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
    ): Pair<Void?, String?>

    // additional data
    suspend fun getAdditionalData(): Pair<AdditionalData?, String?>
    suspend fun addAdditionalData(
        simImage: MultipartBody.Part?,
        stnkImage: MultipartBody.Part?,
        skckImage: MultipartBody.Part?
    ): Pair<Void?, String?>
}