package com.mib.feature_home.contents.order.list

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.DEFAULT_NEXT_CURSOR_REQUEST
import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.domain.model.OrderItemPaging
import com.mib.feature_home.usecase.GetOrdersUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
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
    private val homeNavigation: HomeNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    private val profileNavigation: ProfileNavigation
) : BaseViewModel<OrderListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchOrders(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadHistory = true, shouldShowShimmer = !nextCursor.isNullOrEmpty() && nextCursor == DEFAULT_NEXT_CURSOR_REQUEST)
        viewModelScope.launch(ioDispatcher) {
            val result = getOrdersUseCase(nextCursor)

            withContext(mainDispatcher) {
                result.first?.items.let {
                    state = state.copy(
                        isLoadHistory = false,
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
        goToOrderActionScreen(fragment.findNavController(), item)
    }

    private fun goToOrderActionScreen(
        navController: NavController,
        item: Order
    ) {
        val priceString = item.totalPayment.toString()
        if(item.code != null && priceString != "") {
            homeNavigation.goToOrderActionScreen(
                navController = navController,
                bookingCode = item.code,
                price = priceString,
                bookingDate = item.bookingDate,
                note = item.note
            )
        } else { }
    }

    data class ViewState(
        var isLoadHistory: Boolean = false,
        var shouldShowShimmer: Boolean = false,
        var orderItemPaging: OrderItemPaging? = null
    ) : BaseViewState
}