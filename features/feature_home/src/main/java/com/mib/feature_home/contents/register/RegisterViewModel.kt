package com.mib.feature_home.contents.register

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.Bank
import com.mib.feature_home.domain.model.Location
import com.mib.feature_home.usecase.GetBanksUseCase
import com.mib.feature_home.usecase.GetLocationsUseCase
import com.mib.feature_home.usecase.RegisterUseCase
import com.mib.feature_home.usecase.auth.SendCodeUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.AppUtils.Companion.KEY_HASH_PASSWORD
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val homeNavigation: HomeNavigation,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getBanksUseCase: GetBanksUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<RegisterViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var selectedLocationCode: String? = null
    private var selectedBankCode: String? = null
    private var passwordHash: String? = null

    fun getLocations() {
        viewModelScope.launch(ioDispatcher) {
            val result = getLocationsUseCase()

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        event = EVENT_UPDATE_LOCATION,
                        locations = it
                    )
                }
                result.second?.let {
                }
            }
        }
    }

    fun getBanks() {
        viewModelScope.launch(ioDispatcher) {
            val result = getBanksUseCase()

            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        event = EVENT_UPDATE_BANK,
                        banks = it
                    )
                }
                result.second?.let {
                }
            }
        }
    }

    fun updateSelectedLocation(locationCode: String) {
        selectedLocationCode = locationCode
    }

    fun updateSelectedBank(bankCode: String) {
        selectedBankCode = bankCode
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun register(
        fragment: Fragment,
        email: String,
        password: String,
        name: String,
        bankAccountNumber: String,
        code: String,
        profileFile: MultipartBody.Part?,
        ktpFile: MultipartBody.Part?,
        ktpPersonFile: MultipartBody.Part?
    ) {
        if(!isFormValid(email, password, name, selectedLocationCode, selectedBankCode, bankAccountNumber)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            passwordHash?.let {
                val result = registerUseCase(
                    email,
                    it,
                    name,
                    selectedLocationCode.toString(),
                    profileFile,
                    ktpFile,
                    ktpPersonFile,
                    selectedBankCode.toString(),
                    bankAccountNumber,
                    code
                )

                loadingDialogNavigation.dismiss()
                withContext(mainDispatcher) {
                    result.first?.let {
                        toastEvent.postValue(fragment.context?.getString(R.string.register_succeed))
                        goToHomeScreen(fragment.findNavController())
                    }
                    result.second?.let {
                        toastEvent.postValue(it)
                    }
                }
            }
        }
    }

    fun sendCode(fragment: Fragment, email: String) {
        if(email.isBlank()) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = sendCodeUseCase(email)

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.register_send_code_succeed))
                }
                result.second?.let {
                    toastEvent.postValue(it)
                }
            }
        }
    }

    private fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    fun goToLoginScreen(navController: NavController) {
        homeNavigation.goToLoginScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        email: String,
        password: String? = null,
        name: String? = null,
        locationCode: String? = null,
        bankCode: String? = null,
        bankAccountNumber: String? = null
    ): Boolean {
        passwordHash = password?.let { AppUtils.encode(KEY_HASH_PASSWORD, it) }
        return email.isNotBlank() && password?.isNotBlank() == true && name?.isNotBlank() == true
                && locationCode?.isNotBlank() == true && bankCode?.isNotBlank() == true
                && bankAccountNumber?.isNotBlank() == true
                && !passwordHash.isNullOrBlank()
    }

    data class ViewState(
        var event: Int? = null,
        var locations: List<Location>? = null,
        var banks: List<Bank>? = null
    ) : BaseViewState

    companion object {
        internal const val NO_EVENT = 0
        internal const val EVENT_UPDATE_LOCATION = 1
        internal const val EVENT_UPDATE_BANK = 2
    }
}