package com.mib.feature_home.di

import com.mib.feature_home.repository.HomeRepository
import com.mib.feature_home.repository.HomeRepositoryImpl
import com.mib.feature_home.repository.HomeWithAuthRepository
import com.mib.feature_home.repository.HomeWithAuthRepositoryImpl
import com.mib.feature_home.service.HomeAuthenticatedService
import com.mib.feature_home.service.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object HomeRepositoryModule {
    @Provides
    fun provideHomeRepository(
        homeService: Provider<HomeService>
    ): HomeRepository {
        return HomeRepositoryImpl(homeService::get)
    }

    @Provides
    fun provideHomeRepositoryWithAuth(
        homeAuthenticatedService: Provider<HomeAuthenticatedService>
    ): HomeWithAuthRepository {
        return HomeWithAuthRepositoryImpl(homeAuthenticatedService::get)
    }
}