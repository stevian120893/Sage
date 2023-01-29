package com.mib.lib_api

import android.app.Application
import com.mib.lib_api.util.ResettableLazyManager

object Api {

    internal lateinit var context: Application
        private set
    internal lateinit var apiConfig: ApiConfig
        private set

    internal val resettableManager = ResettableLazyManager()

    private var serviceCache: MutableMap<Class<*>, Any> = mutableMapOf()
    private var serviceWithAuthCache: MutableMap<Class<*>, Any> = mutableMapOf()
    private var serviceBLCache: MutableMap<Class<*>, Any> = mutableMapOf()

    private fun init() {
        serviceCache = mutableMapOf()
        serviceWithAuthCache = mutableMapOf()
        serviceBLCache = mutableMapOf()
        resettableManager.reset()
    }

    /**
     * Call this in [Application.onCreate]
     */
    fun init(apiConfig: ApiConfig) {
        init()
        Api.apiConfig = apiConfig
    }

    /**
     * Call this in [Application.onCreate]
     */
    fun init(application: Application, apiConfig: ApiConfig) {
        init()
        Api.apiConfig = apiConfig
        context = application
    }

    /**
     * This should be used by public API and the AuthenticationService for authenticator
     * to call /auth/refresh, all other authenticated services should use `serviceWithAuth` method
     */
    fun <T> service(klas: Class<T>): T {
        var cachedService: Any? = serviceCache[klas]
        if (cachedService == null) {
            cachedService = RetrofitBuilder.retrofit.create(klas)
            serviceCache[klas] = cachedService as Any
        }
        return cachedService as T
    }

    fun <T> serviceWithAuth(klas: Class<T>): T {
        var cachedService: Any? = serviceWithAuthCache[klas]
        if (cachedService == null) {
            cachedService = RetrofitBuilder.retrofitWithAuth.create(klas)
            serviceWithAuthCache[klas] = cachedService as Any
        }
        return cachedService as T
    }
}
