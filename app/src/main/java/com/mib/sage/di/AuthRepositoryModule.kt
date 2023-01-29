package com.mib.sage.di

import com.mib.lib_auth.repository.AuthenticationRepository
import com.mib.lib_auth.repository.AuthenticationRepositoryImpl
import com.mib.lib_auth.service.AuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    fun provideAuthRepository(
        service: Provider<AuthenticationService>
    ): AuthenticationRepository = AuthenticationRepositoryImpl(service::get)
}