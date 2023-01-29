package com.mib.feature_home.contents.invoice

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.usecase.GetUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SendInvoiceViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val getUseCase: GetUseCase,
) : BaseViewModel<SendInvoiceViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun send(
        fragment: Fragment,
        orderId: String,
        totalPrice: BigDecimal,
    ) {
        if(!isFormValid(orderId, totalPrice.toString())) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        viewModelScope.launch(ioDispatcher) {
            val result = getUseCase()

            withContext(mainDispatcher) {
                if (result.first != null) {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToHomeScreen(fragment.findNavController())
                } else {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_failed_to_save))
                }
            }
        }
    }

    private fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        orderId: String,
        totalPrice: String
    ): Boolean {
        return orderId.isNotBlank() && totalPrice.isNotBlank()
    }

    data class ViewState(
        var icon: String? = null
    ) : BaseViewState
}