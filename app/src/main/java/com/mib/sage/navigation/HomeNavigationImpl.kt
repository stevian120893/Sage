package com.mib.sage.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_CODE
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_DATE
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_NOTE
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_PRICE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_CODE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_DESCRIPTION
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_IMAGE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_NAME
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_PRICE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_STATUS
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_YEAR_EXPERIENCE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_SUBCATEGORY_CODE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_CODE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_DESCRIPTION
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_DISCOUNT_AMOUNT
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_EXPIRED_DATE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_IMAGE_URL
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_INPUT_CODE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_IS_TIME_LIMITED
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_MAXIMUM_DISCOUNT
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_MINIMUM_TRANSACTION
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_QUOTA
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_START_DATE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_STATUS
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_TITLE
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_CATEGORY_ID
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_CATEGORY_NAME
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_SUBCATEGORY_ID
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_SUBCATEGORY_NAME
import com.mib.lib_navigation.HomeNavigation
import com.mib.sage.R
import java.math.BigDecimal

class HomeNavigationImpl : HomeNavigation {
    override fun goToProfileScreen(navController: NavController) {
        navController.navigate(R.id.action_profileFragment)
    }

    override fun goToTukangMenuScreen(navController: NavController) {
        navController.navigate(R.id.action_tukangMenuFragment)
    }

    override fun goToDriverMenuScreen(navController: NavController) {
        navController.navigate(R.id.action_driverMenuFragment)
    }

    override fun goToAddCategoryScreen(navController: NavController, categoryId: String?, categoryName: String?) {
        navController.navigate(
            R.id.action_add_category_fragment,
            bundleOf(
                KEY_CATEGORY_ID to categoryId,
                KEY_CATEGORY_NAME to categoryName
            )
        )
    }

    override fun goToAddSubcategoryScreen(
        navController: NavController,
        categoryId: String?,
        categoryName: String?,
        subcategoryId: String?,
        subcategoryName: String?
    ) {
        navController.navigate(
            R.id.action_add_subcategory_fragment,
            bundleOf(
                KEY_CATEGORY_ID to categoryId,
                KEY_CATEGORY_NAME to categoryName,
                KEY_SUBCATEGORY_ID to subcategoryId,
                KEY_SUBCATEGORY_NAME to subcategoryName,
            )
        )
    }

    override fun goToSetAvailabilityScreen(navController: NavController) {
        navController.navigate(R.id.action_set_availability_fragment)
    }

    override fun goToProductListScreen(navController: NavController) {
        navController.navigate(R.id.action_product_list_fragment)
    }

    override fun goToSendInvoiceScreen(navController: NavController) {
        navController.navigate(R.id.action_send_invoice_fragment)
    }

    override fun goToOrderListScreen(navController: NavController) {
        navController.navigate(R.id.action_order_list_fragment)
    }

    override fun goToOrderActionScreen(
        navController: NavController,
        bookingCode: String,
        price: String,
        bookingDate: String,
        note: String
    ) {
        navController.navigate(
            R.id.action_order_action_fragment,
            bundleOf(
                KEY_ORDER_BOOKING_CODE to bookingCode,
                KEY_ORDER_BOOKING_PRICE to price,
                KEY_ORDER_BOOKING_DATE to bookingDate,
                KEY_ORDER_BOOKING_NOTE to note,
            )
        )
    }

    override fun goToSubscriptionScreen(navController: NavController) {
        navController.navigate(R.id.action_subscription_fragment)
    }

    override fun goToPromoListScreen(navController: NavController) {
        navController.navigate(R.id.action_promo_list_fragment)
    }

    override fun goToCategoryListScreen(navController: NavController) {
        navController.navigate(R.id.action_category_list_fragment)
    }

    override fun goToSubcategoryListScreen(navController: NavController) {
        navController.navigate(R.id.action_subcategory_list_fragment)
    }

    override fun goToProviderScreen(navController: NavController) {
        navController.navigate(R.id.action_provider_fragment)
    }

    override fun goToAddProductScreen(
        navController: NavController,
        subcategoryCode: String?,
        productCode: String?,
        productName: String?,
        productDescription: String?,
        productImage: String?,
        productPrice: String?,
        productYearExperience: Int?,
        productStatus: String?,
    ) {
        navController.navigate(
            R.id.action_add_product_fragment,
            bundleOf(
                KEY_SUBCATEGORY_CODE to subcategoryCode,
                KEY_PRODUCT_CODE to productCode,
                KEY_PRODUCT_NAME to productName,
                KEY_PRODUCT_DESCRIPTION to productDescription,
                KEY_PRODUCT_IMAGE to productImage,
                KEY_PRODUCT_PRICE to productPrice,
                KEY_PRODUCT_YEAR_EXPERIENCE to productYearExperience,
                KEY_PRODUCT_STATUS to productStatus,
            )
        )
    }

    override fun goToAddPromoScreen(
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
    ) {
        navController.navigate(
            R.id.action_add_promo_fragment,
            bundleOf(
                KEY_PROMO_CODE to promoCode,
                KEY_PROMO_TITLE to promoTitle,
                KEY_PROMO_DESCRIPTION to promoDescription,
                KEY_PROMO_DISCOUNT_AMOUNT to promoDiscountAmount,
                KEY_PROMO_MINIMUM_TRANSACTION to minimumTransactionAmount,
                KEY_PROMO_MAXIMUM_DISCOUNT to maximumDiscount,
                KEY_PROMO_INPUT_CODE to promoInputCode,
                KEY_PROMO_QUOTA to promoQuota,
                KEY_PROMO_START_DATE to promoStartDate,
                KEY_PROMO_EXPIRED_DATE to promoExpiredDate,
                KEY_PROMO_IS_TIME_LIMITED to isTimeLimited,
                KEY_PROMO_IMAGE_URL to promoImageUrl,
                KEY_PROMO_STATUS to status,
            )
        )
    }

    override fun goToRegisterScreen(navController: NavController) {
        navController.navigate(R.id.action_register_fragment)
    }

    override fun goToLoginScreen(navController: NavController) {
        navController.navigate(R.id.action_login_fragment)
    }
}