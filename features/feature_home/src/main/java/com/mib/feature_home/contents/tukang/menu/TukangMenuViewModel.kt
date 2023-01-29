package com.mib.feature_home.contents.tukang.menu

import androidx.navigation.NavController
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TukangMenuViewModel @Inject constructor(
    private val homeNavigation: HomeNavigation,
    private val profileNavigation: ProfileNavigation,
) : BaseViewModel<TukangMenuViewModel.ViewState>(ViewState()) {

    fun goToOtherScreen(navController: NavController, destination: String) {
        when (destination) {
            DESTINATION_ADD_CATEGORY -> homeNavigation.goToCategoryListScreen(navController = navController)
            DESTINATION_ADD_SUBCATEGORY -> homeNavigation.goToSubcategoryListScreen(navController = navController)
            DESTINATION_SET_AVAILABILITY -> homeNavigation.goToSetAvailabilityScreen(navController = navController)
            DESTINATION_ADD_PRODUCT -> homeNavigation.goToProductListScreen(navController = navController)
            DESTINATION_ADD_PROMO -> homeNavigation.goToPromoListScreen(navController = navController)
        }
    }

    fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(navController = navController)
    }

    data class ViewState(
        var icon: String? = null
    ) : BaseViewState

    companion object {
        const val DESTINATION_ADD_CATEGORY = "add_category"
        const val DESTINATION_ADD_SUBCATEGORY = "add_subcategory"
        const val DESTINATION_SET_AVAILABILITY = "set_availability"
        const val DESTINATION_ADD_PRODUCT = "add_product"
        const val DESTINATION_ADD_PROMO = "add_promo"
    }
}