package com.mib.sage.di

import com.mib.lib_coroutines.DefaultDispatcher
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @Provides @MainDispatcher
    fun provideMainDispatcher(): CoroutineContext = Dispatchers.Main

    @Provides @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineContext = Dispatchers.Default

    @Provides @IODispatcher
    fun provideIODispatcher(): CoroutineContext = Dispatchers.IO

    @Provides @DelicateCoroutinesApi
    fun provideGlobalScope(): CoroutineScope = GlobalScope
}