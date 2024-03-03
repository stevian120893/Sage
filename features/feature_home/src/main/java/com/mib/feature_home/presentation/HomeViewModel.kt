package com.mib.feature_home.presentation

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mib.feature_home.usecase.auth.SaveFcmTokenUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib.mvvm.utils.DialogUtils
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.DialogListener
import com.mib.lib_navigation.HomeNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val sessionRepository: SessionRepository,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase
) : BaseViewModel<HomeViewModel.ViewState>(ViewState()) {

    fun goToOtherScreen(navController: NavController, destination: String) {
        when (destination) {
            DESTINATION_PROFILE -> homeNavigation.goToProfileScreen(navController = navController)
            DESTINATION_TUKANG -> homeNavigation.goToTukangMenuScreen(navController = navController)
            DESTINATION_DRIVER -> homeNavigation.goToDriverMenuScreen(navController = navController)
            DESTINATION_SEND_INVOICE -> homeNavigation.goToSendInvoiceScreen(navController = navController)
            DESTINATION_MANAGE_ORDER -> homeNavigation.goToOrderListScreen(navController = navController)
            DESTINATION_MANAGE_SUBSCRIPTION -> homeNavigation.goToSubscriptionScreen(navController = navController)
        }
    }

    fun logout(navController: NavController) {
        clearLocalSession()
        goToHomeScreen(navController)
    }

    fun goToHomeScreen(navController: NavController) {
        homeNavigation.goToLoginScreen(navController = navController)
    }

    fun showUploadOptionDialog(fragment: Fragment) {
        DialogUtils.showDialogWithTwoButtons(
            fragment.context,
            "Keluar",
            "Anda yakin ingin keluar?",
            "Ya, keluar",
            "Kembali",
            object : DialogListener {
                override fun onLeftButtonClicked() {
                    logout(fragment.findNavController())
                }
                override fun onRightButtonClicked() {}
            }
        )
    }

    fun setFirebaseToken() {
        val isLoggedIn = !sessionRepository.getAccessToken().isNullOrBlank()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            if(isLoggedIn) {
                if(sessionRepository.getFcmToken() != token)
                    saveFcmToken(token)
            }
        })
    }

    private fun saveFcmToken(token: String) {
        viewModelScope.launch(ioDispatcher) {
            val result = saveFcmTokenUseCase(token)

            withContext(mainDispatcher) {
                result.first?.let {
                    sessionRepository.saveFcmToken(token)
                }
                result.second?.let {
                    // failed to save fcm token to server
                    toastEvent.postValue(it)
                }
            }
        }
    }

    private fun clearLocalSession() {
        sessionRepository.clearLocalSession()
    }

    data class ViewState(
        var icon: String? = null
    ) : BaseViewState

    companion object {
        const val DESTINATION_PROFILE = "profile"
        const val DESTINATION_TUKANG = "tukang"
        const val DESTINATION_DRIVER = "driver"
        const val DESTINATION_SEND_INVOICE = "send_invoice"
        const val DESTINATION_MANAGE_ORDER = "manage_order"
        const val DESTINATION_MANAGE_SUBSCRIPTION = "manage_subscription"
    }
}