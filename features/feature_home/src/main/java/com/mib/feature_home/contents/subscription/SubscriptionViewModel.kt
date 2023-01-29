package com.mib.feature_home.contents.subscription

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.AdminBank
import com.mib.feature_home.domain.model.SubscriptionOrdersItemPaging
import com.mib.feature_home.domain.model.SubscriptionType
import com.mib.feature_home.domain.model.UserSubscription
import com.mib.feature_home.usecase.BuySubscriptionUseCase
import com.mib.feature_home.usecase.GetBankInfoUseCase
import com.mib.feature_home.usecase.GetSubscriptionOrdersUseCase
import com.mib.feature_home.usecase.GetSubscriptionTypeUseCase
import com.mib.feature_home.usecase.GetUserSubscriptionUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val getSubscriptionTypeUseCase: GetSubscriptionTypeUseCase,
    private val getUserSubscriptionUseCase: GetUserSubscriptionUseCase,
    private val buySubscriptionUseCase: BuySubscriptionUseCase,
    private val getBankInfoUseCase: GetBankInfoUseCase,
    private val getSubscriptionOrdersUseCase: GetSubscriptionOrdersUseCase,
    val loadingDialogNavigation: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<SubscriptionViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var selectedSubscriptionType: String? = null

    fun fetchData(fragment: Fragment) {
        viewModelScope.launch(ioDispatcher) {
            val getUserSubscriptionAsync = async { getUserSubscriptionUseCase() }
            val getSubscriptionTypeAsync = async { getSubscriptionTypeUseCase() }
            val getBankInfoAsync = async { getBankInfoUseCase() }
            val getSubscriptionOrdersAsync = async { getSubscriptionOrdersUseCase() }

            val userSubscriptionResult = getUserSubscriptionAsync.await()
            val subscriptionTypeResult = getSubscriptionTypeAsync.await()
            val bankInfoResult = getBankInfoAsync.await()
            val subscriptionOrdersResult = getSubscriptionOrdersAsync.await()

            val errorMessage = subscriptionTypeResult.second ?: bankInfoResult.second
            userSubscriptionResult.second?.let {
                state = state.copy(
                    subscriptionMessage = it
                )
                if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                    toastEvent.postValue(it)
                    withContext(mainDispatcher) {
                        unauthorizedErrorNavigation.handleErrorMessage(fragment.findNavController(), it)
                    }
                }
            }
            errorMessage?.let {
                toastEvent.postValue(it)
                if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                    toastEvent.postValue(it)
                    withContext(mainDispatcher) {
                        unauthorizedErrorNavigation.handleErrorMessage(fragment.findNavController(), it)
                    }
                }
            }

            loadingDialogNavigation.dismiss()
            state = state.copy(
                subscriptionTypes = subscriptionTypeResult.first,
                userSubscription = userSubscriptionResult.first,
                subscriptionMessage = userSubscriptionResult.second,
                adminBank = bankInfoResult.first,
                subscriptionOrdersItemPaging = subscriptionOrdersResult.first
            )
        }
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun buy(
        fragment: Fragment,
        image: MultipartBody.Part?,
        referralCode: String? = null
    ) {
        if(selectedSubscriptionType.isNullOrBlank()) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }
        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = buySubscriptionUseCase(selectedSubscriptionType.orEmpty(), image, referralCode)
            loadingDialogNavigation.dismiss()

            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.buy_subscription_succeed))
                    goToHomeScreen(fragment.findNavController())
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

    fun updateSelectedSubscription(subscriptionCode: String) {
        selectedSubscriptionType = subscriptionCode
    }

    fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    data class ViewState(
        var subscriptionTypes: List<SubscriptionType>? = null,
        var userSubscription: UserSubscription? = null,
        var subscriptionMessage: String? = null,
        var adminBank: AdminBank? = null,
        var subscriptionOrdersItemPaging: SubscriptionOrdersItemPaging? = null
    ) : BaseViewState
}