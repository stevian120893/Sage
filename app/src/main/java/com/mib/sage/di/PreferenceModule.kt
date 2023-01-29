package com.mib.sage.di

import android.app.Application
import com.mib.lib_auth.storage.SecureSessionPref
import com.mib.lib_pref.AccountPref
import com.mib.lib_pref.SessionPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
    @Provides
    fun provideSecureSessionPref(app: Application): SecureSessionPref = SecureSessionPref.get(app)

    @Provides
    fun provideSessionPref(): SessionPref = SessionPref.get()

    @Provides
    fun provideAccountPref(): AccountPref = AccountPref.get()

//    @Provides
//    fun provideHomePref(): HomePref = HomePref.get()
//
//    @Provides
//    fun provideAppUpdatePref(): AppUpdatePref = AppUpdatePref.get()
//
//    @Provides
//    fun provideAdjustPref(): AdjustPref = AdjustPref.get()
//
//    @Provides
//    fun provideReceiptPref(): ReceiptPref = ReceiptPref.get()
}