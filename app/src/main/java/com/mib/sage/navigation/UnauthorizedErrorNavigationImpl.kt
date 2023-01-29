package com.mib.sage.navigation

import androidx.navigation.NavController
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.sage.R

class UnauthorizedErrorNavigationImpl(
    private val sessionRepository: SessionRepository,
) : UnauthorizedErrorNavigation {
    override fun handleErrorMessage(navController: NavController, errorMessage: String) {
        sessionRepository.clearLocalSession()
        navController.navigate(R.id.action_login_fragment)
    }
}