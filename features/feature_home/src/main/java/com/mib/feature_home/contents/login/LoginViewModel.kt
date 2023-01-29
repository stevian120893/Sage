package com.mib.feature_home.contents.login

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_auth.usecase.LoginUseCase
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val homeNavigation: HomeNavigation,
    private val loginUseCase: LoginUseCase,
    val loadingDialog: LoadingDialogNavigation
) : BaseViewModel<LoginViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private var passwordHash: String? = null

    fun login(
        fragment: Fragment,
        email: String,
        password: String
    ) {
        if(!isFormValid(email, password)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            passwordHash?.let { password ->
                val result = loginUseCase(email, password)

                loadingDialog.dismiss()
                withContext(mainDispatcher) {
                    result.first?.let {
                        goToHomeScreen(fragment.findNavController())
                    }
                    result.second?.let {
                        toastEvent.postValue(it)
                    }
                }
            }
        }
    }

    fun goToRegisterScreen(navController: NavController) {
        homeNavigation.goToRegisterScreen(
            navController = navController
        )
    }

    private fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        email: String,
        password: String
    ): Boolean {
        passwordHash = password.let { AppUtils.encode(AppUtils.KEY_HASH_PASSWORD, it) }
        return email.isNotBlank() && !passwordHash.isNullOrBlank()
    }

    data class ViewState(
        var icon: String? = null
    ) : BaseViewState
}