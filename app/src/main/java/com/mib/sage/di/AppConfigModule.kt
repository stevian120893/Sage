package com.mib.sage.di

import android.app.Application
import com.mib.lib_auth.AccessTokenAuthenticator
import com.mib.sage.config.ApiAppConfig
import com.mib.sage.config.SageAppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @ApiConfigQualifier
    fun provideApiConfig(
        app: Application,
        @BaseUrlQualifier baseUrl: String,
        @AccessTokenInterceptorQualifier accessTokenInterceptor: Interceptor,
        accessTokenAuthenticator: Provider<AccessTokenAuthenticator>
    ): SageAppConfig {
        return ApiAppConfig(
            app,
            baseUrl,
            accessTokenInterceptor = accessTokenInterceptor,
            accessTokenAuthenticator = accessTokenAuthenticator::get
        )
    }

    //    @Provides
    //    fun provideNotificationConfig(app: Application): NotificationConfig = NotificationConfig(app)
}
