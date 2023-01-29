package com.mib.sage.di

import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
import com.mib.sage.navigation.UnauthorizedErrorNavigationImpl
import com.mib.sage.navigation.HomeNavigationImpl
import com.mib.sage.navigation.LoadingDialogNavigationImpl
import com.mib.sage.navigation.ProfileNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    fun provideHomeNavigation(): HomeNavigation = HomeNavigationImpl()

    @Provides
    fun provideProfileNavigation(): ProfileNavigation = ProfileNavigationImpl()

    @Provides
    fun provideLoadingDialogNavigation(): LoadingDialogNavigation = LoadingDialogNavigationImpl()

    @Provides
    fun provideUnauthorizedErrorNavigation(
        sessionRepository: SessionRepository,
    ): UnauthorizedErrorNavigation {
        return UnauthorizedErrorNavigationImpl(sessionRepository)
    }
}
