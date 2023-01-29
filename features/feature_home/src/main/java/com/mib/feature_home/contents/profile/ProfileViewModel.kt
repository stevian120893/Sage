package com.mib.feature_home.contents.profile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.usecase.GetProfileUseCase
import com.mib.feature_home.usecase.SaveProfileUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.UnauthorizedErrorNavigation
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
class ProfileViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<ProfileViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var isAcceptCash: Boolean = false
    private var isAcceptBankTransfer: Boolean = false

    fun getProfile(fragment: Fragment) {
        viewModelScope.launch(ioDispatcher) {
            val result = getProfileUseCase()
            loadingDialog.dismiss()

            result.first?.let {
                state = state.copy(
                    profile = result.first
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

    fun saveProfile(
        fragment: Fragment,
        name: String,
        profileFile: MultipartBody.Part?
    ) {
        if(!isFormValid(name)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            val result = saveProfileUseCase(
                name,
                state.profile?.cityCode.orEmpty(),
                state.profile?.bankCode.orEmpty(),
                state.profile?.bankAccountNumber.orEmpty(),
                isAcceptCash,
                isAcceptBankTransfer,
                profileFile
            )

            loadingDialog.dismiss()
            withContext(mainDispatcher) {
                if (result.first != null) {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                } else {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_failed_to_save))
                }
            }
        }
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun updatePayment(isAcceptCash: Boolean? = null, isAcceptBankTransfer: Boolean? = null)  {
        isAcceptCash?.let { this.isAcceptCash = it }
        isAcceptBankTransfer?.let { this.isAcceptBankTransfer = it }
    }

    fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    fun copyText(context: Context, text: String) {
        AppUtils.copyToClipboard(context, text)
        toastEvent.postValue(context.getString(R.string.shared_res_copied))
    }

    private fun isFormValid(name: String): Boolean {
        return name.isNotBlank()
    }

    data class ViewState(
        var profile: Profile? = null
    ) : BaseViewState
}