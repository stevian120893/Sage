package com.mib.feature_home.contents.order.list

import com.mib.feature_home.usecase.GetUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OrderListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val getUseCase: GetUseCase,
) : BaseViewModel<OrderListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private fun isFormValid(
        categoryName: String
    ): Boolean {
        return categoryName.isNotBlank()
    }

    data class ViewState(
        var icon: String? = null
    ) : BaseViewState
}