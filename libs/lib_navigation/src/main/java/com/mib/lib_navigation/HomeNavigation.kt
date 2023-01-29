package com.mib.lib_navigation

import androidx.navigation.NavController
import java.math.BigDecimal

interface HomeNavigation {
    fun goToProfileScreen(navController: NavController)
    fun goToTukangMenuScreen(navController: NavController)
    fun goToDriverMenuScreen(navController: NavController)
    fun goToAddCategoryScreen(navController: NavController, categoryId: String?, categoryName: String?)
    fun goToAddSubcategoryScreen(
        navController: NavController,
        categoryId: String?,
        categoryName: String?,
        subcategoryId: String?,
        subcategoryName: String?
    )
    fun goToSetAvailabilityScreen(navController: NavController)
    fun goToProductListScreen(navController: NavController)
    fun goToSendInvoiceScreen(navController: NavController)
    fun goToOrderListScreen(navController: NavController)
    fun goToSubscriptionScreen(navController: NavController)
    fun goToPromoListScreen(navController: NavController)
    fun goToCategoryListScreen(navController: NavController)
    fun goToSubcategoryListScreen(navController: NavController)
    fun goToProviderScreen(navController: NavController)
    fun goToAddProductScreen(
        navController: NavController,
        subcategoryCode: String?,
        productCode: String?,
        productName: String?,
        productDescription: String?,
        productImage: String?,
        productPrice: String?,
        productYearExperience: Int?,
        productStatus: String?,
    )
    fun goToAddPromoScreen(
        navController: NavController,
        promoCode: String?,
        promoTitle: String?,
        promoDescription: String?,
        promoDiscountAmount: BigDecimal?,
        minimumTransactionAmount: BigDecimal?,
        maximumDiscount: BigDecimal?,
        promoInputCode: String?,
        promoQuota: Int?,
        promoStartDate: String?,
        promoExpiredDate: String?,
        isTimeLimited: Boolean?,
        promoImageUrl: String?,
        status: String?
    )
    fun goToRegisterScreen(navController: NavController)
    fun goToLoginScreen(navController: NavController)
}