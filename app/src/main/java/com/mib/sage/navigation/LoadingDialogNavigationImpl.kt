package com.mib.sage.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_util.SingleLiveEvent
import com.mib.sage.R

class LoadingDialogNavigationImpl : LoadingDialogNavigation {

    private val _shouldShowDialog: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private var counter: Int = 0

    override fun subscribe(fragment: Fragment, shouldShowDialog: Boolean) {
        _shouldShowDialog.observe(fragment.viewLifecycleOwner) { show ->
            val navController = fragment.findNavController()
            if (show && !isLoadingDialogShown(navController)) {
                navController.navigate(R.id.action_loadingDialog)
            } else if (!show && isLoadingDialogShown(navController)) {
                navController.popBackStack()
            }
        }
        if (shouldShowDialog) {
            show()
        } else {
            dismiss()
            counter = 0
        }
    }

    override fun show() {
        counter++
        _shouldShowDialog.postValue(true)
    }

    override fun dismiss() {
        counter--
        if (counter == 0) _shouldShowDialog.postValue(false)
    }

    private fun isLoadingDialogShown(navController: NavController): Boolean {
        return navController.currentDestination?.id == R.id.loadingDialog
    }
}