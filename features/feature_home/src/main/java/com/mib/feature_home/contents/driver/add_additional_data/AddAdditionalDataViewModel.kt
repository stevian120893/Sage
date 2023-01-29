package com.mib.feature_home.contents.driver.add_additional_data

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.AdditionalData
import com.mib.feature_home.usecase.AddAdditionalDataUseCase
import com.mib.feature_home.usecase.GetAdditionalDataUseCase
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
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage

@HiltViewModel
class AddAdditionalDataViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    val loadingDialog: LoadingDialogNavigation,
    val getAdditionalDataUseCase: GetAdditionalDataUseCase,
    val addAdditionalDataUseCase: AddAdditionalDataUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<AddAdditionalDataViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun getAdditionalData(fragment: Fragment) {
        viewModelScope.launch(ioDispatcher) {
            val result = getAdditionalDataUseCase()
            loadingDialog.dismiss()

            result.first?.let {
                state = state.copy(
                    additionalData = result.first
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

    fun sendAdditionalData(
        fragment: Fragment,
        simPhotoFile: MultipartBody.Part?,
        stnkPhotoFile: MultipartBody.Part?,
        skckPhotoFile: MultipartBody.Part?
    ) {
        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            val result = addAdditionalDataUseCase(
                simPhotoFile,
                stnkPhotoFile,
                skckPhotoFile
            )

            loadingDialog.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToHomeScreen(fragment.findNavController())
                }
                result.second?.let {
                    toastEvent.postValue(it)
                }
            }
        }
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    data class ViewState(
        var additionalData: AdditionalData? = null
    ) : BaseViewState
}