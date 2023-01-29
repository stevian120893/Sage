package com.mib.lib_navigation

import androidx.fragment.app.Fragment

interface LoadingDialogNavigation {
    /**
     * Call this in [Fragment.onViewCreated]
     */
    fun subscribe(fragment: Fragment, shouldShowDialog: Boolean)

    fun show()

    fun dismiss()
}