package com.mib.sage

import com.mib.sage.config.ApiAppConfig
import com.mib.sage.di.BaseUrlQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReleaseDiModule {
    @Provides
    @BaseUrlQualifier
    fun provideBaseUrl(): String = ApiAppConfig.PREPRODUCTION_BASE_URL
}
