package com.mib.feature_home.contents.order.action

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.order.action.OrderActionFragment.Companion.KEY_PAYMENT_METHOD_CASH
import com.mib.feature_home.contents.order.action.OrderActionFragment.Companion.KEY_PAYMENT_METHOD_DANA
import com.mib.feature_home.contents.order.action.OrderActionFragment.Companion.KEY_PAYMENT_METHOD_TRANSFER
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_CODE
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_DATE
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_NOTE
import com.mib.feature_home.contents.order.list.OrderListFragment.Companion.KEY_ORDER_BOOKING_PRICE
import com.mib.feature_home.domain.model.PaymentMethod
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.usecase.ApproveOrderUseCase
import com.mib.feature_home.usecase.CancelOrderUseCase
import com.mib.feature_home.usecase.DoneOrderUseCase
import com.mib.feature_home.usecase.GetOrderDetailUseCase
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.aprilapps.easyphotopicker.EasyImage
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OrderActionViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val approveOrderUseCase: ApproveOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val doneOrderUseCase: DoneOrderUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<OrderActionViewModel.ViewState>(ViewState(NO_EVENT)) {

    private var selectedPaymentMethod: String? = null

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun init(arg: Bundle?, fragment: Fragment) {
        state = state.copy(
            bookingCode = arg?.getString(KEY_ORDER_BOOKING_CODE),
            price = arg?.getString(KEY_ORDER_BOOKING_PRICE).orEmpty(),
            bookingDate = arg?.getString(KEY_ORDER_BOOKING_DATE).orEmpty(),
            note = arg?.getString(KEY_ORDER_BOOKING_NOTE).orEmpty(),
            paymentMethod = listOf(
                PaymentMethod(fragment.context?.getString(R.string.payment_method_dana).orEmpty(), KEY_PAYMENT_METHOD_DANA),
                PaymentMethod(fragment.context?.getString(R.string.payment_method_transfer).orEmpty(), KEY_PAYMENT_METHOD_TRANSFER),
                PaymentMethod(fragment.context?.getString(R.string.payment_method_cash).orEmpty(), KEY_PAYMENT_METHOD_CASH)
            )
        )
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun getOrderDetail(navController: NavController) {
        state = state.copy(isLoadOrderDetail = true, event = EVENT_UPDATE_ORDER_DETAIL)
        viewModelScope.launch(ioDispatcher) {
            val result = getOrderDetailUseCase(state.bookingCode.orEmpty())

            withContext(mainDispatcher) {
                result.first?.let {
                    val isOrderDone = it.status == OrderDetail.DONE
                    state = if(isOrderDone) {
                        state.copy(
                            event = EVENT_ORDER_SUCCEED,
                            isLoadOrderDetail = false,
                            orderDetail = it
                        )
                    } else {
                        state.copy(
                            event = EVENT_UPDATE_ORDER_DETAIL,
                            isLoadOrderDetail = false,
                            orderDetail = it
                        )
                    }
                }
                result.second?.let {
                    toastEvent.postValue(it)
                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                        withContext(mainDispatcher) {
                            unauthorizedErrorNavigation.handleErrorMessage(navController, it)
                        }
                    }
                }
            }
        }
    }

    fun approveOrder(fragment: Fragment, price: String, bookingDate: String, note: String) {
        if(state.bookingCode == null) return

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = approveOrderUseCase.invoke(
                code = state.bookingCode.orEmpty(),
                paymentMethod = selectedPaymentMethod.orEmpty(),
                price = price,
                bookingDate = bookingDate,
                note = note
            )

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToOrderListScreen(fragment.findNavController())
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

    fun cancelOrder(fragment: Fragment) {
        if(state.bookingCode == null) return

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = cancelOrderUseCase.invoke(code = state.bookingCode.orEmpty())


            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToOrderListScreen(fragment.findNavController())
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

    fun doneOrder(fragment: Fragment) {
        if(state.bookingCode == null) return

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = doneOrderUseCase.invoke(code = state.bookingCode.orEmpty())


            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToOrderListScreen(fragment.findNavController())
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

    fun updateSelectedPaymentMethod(paymentMethod: String) {
        selectedPaymentMethod = paymentMethod
    }

    fun goToOrderListScreen(navController: NavController) {
        homeNavigation.goToOrderListScreen(navController = navController)
    }

    data class ViewState(
        val event: Int,
        var isLoadOrderDetail: Boolean = false,
        var orderDetail: OrderDetail? = null,
        var bookingCode: String? = null,
        var price: String? = null,
        var bookingDate: String? = null,
        var note: String? = null,
        var paymentMethod: List<PaymentMethod>? = null,
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 1
        const val EVENT_UPDATE_ORDER_DETAIL = 2
        const val EVENT_ORDER_SUCCEED = 3
    }
}