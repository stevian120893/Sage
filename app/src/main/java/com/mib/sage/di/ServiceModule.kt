package com.mib.sage.di

import com.mib.lib_api.Api
import com.mib.lib_auth.service.AuthenticationService
import com.mib.lib_user.service.UserAuthenticatedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideAuthenticationService(): AuthenticationService = Api.service(AuthenticationService::class.java)

    @Provides
    fun provideUserAuthenticatedService(): UserAuthenticatedService = Api.serviceWithAuth(
        UserAuthenticatedService::class.java)
}
