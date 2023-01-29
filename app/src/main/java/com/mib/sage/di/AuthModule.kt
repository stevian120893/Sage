package com.mib.sage.di

import com.mib.lib_auth.AccessTokenAuthenticator
import com.mib.lib_auth.AccessTokenInterceptor
import com.mib.lib_auth.repository.AuthenticationRepository
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_auth.repository.SessionRepositoryImpl
import com.mib.lib_auth.service.AuthenticationService
import com.mib.lib_auth.storage.SecureSessionPref
import com.mib.lib_auth.usecase.LoginUseCase
import com.mib.lib_pref.SessionPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideSessionRepository(
        secureSessionPref: SecureSessionPref,
        sessionPref: SessionPref
    ): SessionRepository {
        return SessionRepositoryImpl(secureSessionPref, sessionPref)
    }

    @Provides @AccessTokenInterceptorQualifier
    fun provideAccessTokenInterceptor(repo: SessionRepository): Interceptor {
        return AccessTokenInterceptor(repo)
    }

    @Provides
    fun provideAccessTokenAuthenticator(
        repo: SessionRepository,
        service: AuthenticationService
    ): AccessTokenAuthenticator {
        return AccessTokenAuthenticator(repo, service)
    }

    @Provides
    fun provideLoginUseCase(
        authRepo: AuthenticationRepository,
        sessRepo: SessionRepository
    ): LoginUseCase {
        return LoginUseCase(authRepo, sessRepo)
    }
}