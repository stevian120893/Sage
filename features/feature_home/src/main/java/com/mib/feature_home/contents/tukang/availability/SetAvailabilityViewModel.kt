package com.mib.feature_home.contents.tukang.availability

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.AvailabilityDay
import com.mib.feature_home.usecase.GetAvailabilityDaysUseCase
import com.mib.feature_home.usecase.GetUseCase
import com.mib.feature_home.usecase.SetAvailabilityDaysUseCase
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
class SetAvailabilityViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val setAvailabilityUseCase: SetAvailabilityDaysUseCase,
    private val getAvailabilityDaysUseCase: GetAvailabilityDaysUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<SetAvailabilityViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchAvailabilityDays(fragment: Fragment) {
        viewModelScope.launch(ioDispatcher) {
            val result = getAvailabilityDaysUseCase()

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        event = EVENT_UPDATE_FORM,
                        availabilityDays = it
                    )
//                    _availabilityDaysLiveData.postValue(
//                        availDays.associateBy(
//                            { it.availabilityDayCode },
//                            {
//                                AvailabilityDay(
//                                    availabilityDayCode = it.availabilityDayCode,
//                                    dayName = it.dayName,
//                                    openHour = it.openHour,
//                                    closedHour = it.closedHour,
//                                    isClosed = it.isClosed
//                                )
//                            }
//                        )
//                    )
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

    fun save(
        fragment: Fragment,
        availabilityDays: List<AvailabilityDay>
    ) {
        viewModelScope.launch(ioDispatcher) {
            val result = setAvailabilityUseCase(availabilityDays)

            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
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

    fun goToTukangMenuScreen(navController: NavController) {
        homeNavigation.goToTukangMenuScreen(
            navController = navController
        )
    }

    data class ViewState(
        var event: Int? = null,
        var availabilityDays: List<AvailabilityDay>? = null
    ) : BaseViewState

    companion object {
        internal const val NO_EVENT = 0
        internal const val EVENT_UPDATE_FORM = 1
    }
}