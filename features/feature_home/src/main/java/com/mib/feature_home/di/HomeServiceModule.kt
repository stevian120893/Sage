package com.mib.feature_home.di

import com.mib.feature_home.service.HomeAuthenticatedService
import com.mib.feature_home.service.HomeService
import com.mib.lib_api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HomeServiceModule {
    @Provides
    fun provideHomeService(): HomeService =
        Api.service(HomeService::class.java)

    @Provides
    fun provideHomeAuthenticatedService(): HomeAuthenticatedService =
        Api.serviceWithAuth(HomeAuthenticatedService::class.java)
}