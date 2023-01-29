package com.mib.lib_navigation

import androidx.navigation.NavController

interface UnauthorizedErrorNavigation {
    fun handleErrorMessage(navController: NavController, errorMessage: String)
}