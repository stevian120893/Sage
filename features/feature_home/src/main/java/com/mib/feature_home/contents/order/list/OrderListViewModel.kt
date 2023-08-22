package com.mib.feature_home.contents.order.list

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.domain.model.OrderItemPaging
import com.mib.feature_home.usecase.GetOrdersUseCase
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
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class OrderListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val getOrdersUseCase: GetOrdersUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val homeNavigation: HomeNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    private val profileNavigation: ProfileNavigation
) : BaseViewModel<OrderListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchOrders(fragment: Fragment, nextCursor: String? = null) {
        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            val result = getOrdersUseCase(
                cursor = nextCursor
            )
            loadingDialog.dismiss()

            withContext(mainDispatcher) {
                result.first?.items.let {
                    state = state.copy(
                        event = EVENT_UPDATE_ORDERS,
                        orderItemPaging = result.first
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

    fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(navController = navController)
    }

    fun onItemClick(
        fragment: Fragment,
        item: Order
    ) {
//        goToAddProductScreen(fragment, item)
    }

    data class ViewState(
        var event: Int? = null,
        var orderItemPaging: OrderItemPaging? = null
    ) : BaseViewState

    companion object {
        internal const val NO_EVENT = 0
        internal const val EVENT_UPDATE_ORDERS = 1
    }
}