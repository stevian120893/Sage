package com.mib.feature_home.contents.tukang.promo.list

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.domain.model.PromosItemPaging
import com.mib.feature_home.usecase.GetPromosUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PromoListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getPromosUseCase: GetPromosUseCase,
    val loadingDialog: LoadingDialogNavigation,
    val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<PromoListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchPromos(fragment: Fragment, nextCursor: String? = null) {
        viewModelScope.launch(ioDispatcher) {
            val result = getPromosUseCase(nextCursor)

            loadingDialog.dismiss()
            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        promosItemPaging = result.first
                    )
                }
                result.second?.let {
                    toastEvent.postValue(it)
                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                        withContext(mainDispatcher) {
                            unauthorizedErrorNavigation.handleErrorMessage(fragment.findNavController(), it)
                        }
                    }
                }
            }
        }
    }

    fun goToAddPromoScreen(navController: NavController, item: Promo? = null) {
        homeNavigation.goToAddPromoScreen(
            navController = navController,
            promoCode = item?.promoCode,
            promoTitle = item?.promoTitle,
            promoDescription = item?.promoDescription,
            promoDiscountAmount = item?.promoDiscountAmount,
            minimumTransactionAmount = item?.minimumTransactionAmount,
            maximumDiscount = item?.maximumDiscount,
            promoInputCode = item?.promoInputCode,
            promoQuota = item?.promoQuota,
            promoStartDate = item?.promoStartDate,
            promoExpiredDate = item?.promoExpiredDate,
            isTimeLimited = item?.isTimeLimited,
            promoImageUrl = item?.promoImageUrl,
            status = item?.status
        )
    }

    fun onItemClick(
        fragment: Fragment,
        item: Promo
    ) {
        goToAddPromoScreen(fragment.findNavController(), item)
    }

    fun goToTukangMenuScreen(navController: NavController) {
        homeNavigation.goToTukangMenuScreen(
            navController = navController
        )
    }

    data class ViewState(
        var promosItemPaging: PromosItemPaging? = null
    ) : BaseViewState
}