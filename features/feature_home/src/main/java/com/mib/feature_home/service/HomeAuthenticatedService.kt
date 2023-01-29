package com.mib.feature_home.service

import com.mib.feature_home.dto.request.AddCategoryRequest
import com.mib.feature_home.dto.request.AddProductRequest
import com.mib.feature_home.dto.request.AddPromoRequest
import com.mib.feature_home.dto.request.AddSubcategoryRequest
import com.mib.feature_home.dto.request.AvailabilityDayRequest
import com.mib.feature_home.dto.request.SetAvailabilityRequest
import com.mib.feature_home.dto.response.AdditionalDataResponse
import com.mib.feature_home.dto.response.AdminBankResponse
import com.mib.feature_home.dto.response.AvailabilityDayResponse
import com.mib.feature_home.dto.response.BuySubscriptionResponse
import com.mib.feature_home.dto.response.CategoryResponse
import com.mib.feature_home.dto.response.ProductResponse
import com.mib.feature_home.dto.response.ProfileResponse
import com.mib.feature_home.dto.response.PromoResponse
import com.mib.feature_home.dto.response.SubcategoryResponse
import com.mib.feature_home.dto.response.SubscriptionOrderResponse
import com.mib.feature_home.dto.response.SubscriptionTypeResponse
import com.mib.feature_home.dto.response.UserSubscriptionResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface HomeAuthenticatedService {
    @GET("/profile")
    suspend fun getProfile(
    ): NetworkResponse<ApiResponse<ProfileResponse>>

    @Multipart
    @POST("/profile/update")
    suspend fun saveProfile(
        @Part("name") name: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("bank_code") bankCode: RequestBody?,
        @Part("bank_account_number") bankAccountNumber: RequestBody?,
        @Part("is_accept_cash") isAcceptCash: RequestBody?,
        @Part("is_accept_bank_transfer") isAcceptBankTransfer: RequestBody?,
        @Part profilePicture: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<ProfileResponse>>

    @GET("/category/list")
    suspend fun getCategories(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<CategoryResponse>>>

    @POST("/category/add")
    suspend fun addCategory(
        @Body body: AddCategoryRequest
    ): NetworkResponse<ApiResponse<Void>>

    @POST("/category/edit")
    suspend fun editCategory(
        @Body body: AddCategoryRequest
    ): NetworkResponse<ApiResponse<Void>>

    @GET("/subcategory/list")
    suspend fun getSubcategories(
        @Query("category_code") categoryId: String,
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<SubcategoryResponse>>>

    @POST("/subcategory/add")
    suspend fun addSubcategory(
        @Body body: AddSubcategoryRequest
    ): NetworkResponse<ApiResponse<Void>>

    @POST("/subcategory/edit")
    suspend fun editSubcategory(
        @Body body: AddSubcategoryRequest
    ): NetworkResponse<ApiResponse<Void>>

    @GET("/merchant/get-day-list")
    suspend fun getAvailabilityDays(
    ): NetworkResponse<ApiResponse<List<AvailabilityDayResponse>>>

    @POST("/merchant/set-day-list")
    suspend fun setAvailabilityDays(
        @Body body: List<AvailabilityDayRequest>
    ): NetworkResponse<ApiResponse<List<AvailabilityDayResponse>>>

    @GET("/product/list")
    suspend fun getProducts(
        @Query("category_code") categoryId: String,
        @Query("subcategory_code") subcategoryId: String,
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<ProductResponse>>>

    @Multipart
    @POST("/product/add")
    suspend fun addProduct(
        @Part("subcategory_code") subcategoryCode: RequestBody? = null,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("year_experience") yearExperience: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("code") productCode: RequestBody? = null
    ): NetworkResponse<ApiResponse<Void>>

    @Multipart
    @POST("/product/edit")
    suspend fun editProduct(
        @Part("code") productCode: RequestBody? = null,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("year_experience") yearExperience: RequestBody?,
        @Part image: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<Void>>

    @GET("/subscription/list")
    suspend fun getSubscriptionTypes(
    ): NetworkResponse<ApiResponse<List<SubscriptionTypeResponse>>>

    @GET("/subscription/history")
    suspend fun getSubscriptionOrders(
    ): NetworkResponse<ApiResponse<List<SubscriptionOrderResponse>>>

    @GET("/subscription/current")
    suspend fun getUserSubscription(
    ): NetworkResponse<ApiResponse<UserSubscriptionResponse>>

    @GET("/public/get-admin-bank")
    suspend fun getAdminBank(
    ): NetworkResponse<ApiResponse<List<AdminBankResponse>>>

    @Multipart
    @POST("/subscription/add")
    suspend fun buySubscription(
        @Part("code") code: RequestBody?,
        @Part("referral_code") referralCode: RequestBody?,
        @Part image: MultipartBody.Part?,
    ): NetworkResponse<ApiResponse<BuySubscriptionResponse>>

    @GET("/promo/list")
    suspend fun getPromos(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<PromoResponse>>>

    @Multipart
    @POST("/promo/create")
    suspend fun addPromo(
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("input_code") inputCode: RequestBody?,
        @Part("quota") quota: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("discount") discount: RequestBody?,
        @Part("minimum_amount") minimumAmount: RequestBody?,
        @Part("maximum_discount") maximumDiscount: RequestBody?,
        @Part("start_date") startDate: RequestBody?,
        @Part("end_date") endDate: RequestBody?,
        @Part("is_time_limited") isTimeLimited: RequestBody?,
        @Part image: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<Void>>

    @Multipart
    @POST("/promo/edit")
    suspend fun editPromo(
        @Part("promo_code") promoCode: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("input_code") inputCode: RequestBody?,
        @Part("quota") quota: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("discount") discount: RequestBody?,
        @Part("minimum_amount") minimumAmount: RequestBody?,
        @Part("maximum_discount") maximumDiscount: RequestBody?,
        @Part("start_date") startDate: RequestBody?,
        @Part("end_date") endDate: RequestBody?,
        @Part("is_time_limited") isTimeLimited: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part image: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<Void>>

    @GET("/profile/get-additional-data")
    suspend fun getAdditionalData(): NetworkResponse<ApiResponse<AdditionalDataResponse>>

    @Multipart
    @POST("/profile/send-additional-data")
    suspend fun addAdditionalData(
        @Part simImage: MultipartBody.Part?,
        @Part stnkImage: MultipartBody.Part?,
        @Part skckImage: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<Void>>
}
