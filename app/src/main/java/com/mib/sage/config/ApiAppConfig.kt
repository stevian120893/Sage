package com.mib.sage.config

import android.app.Application
import com.mib.lib_api.Api
import com.mib.lib_api.ApiConfig
import okhttp3.Authenticator
import okhttp3.Interceptor

class ApiAppConfig(
    private val app: Application,
    private val baseUrl: String,
    private val accessTokenInterceptor: Interceptor,
    private val accessTokenAuthenticator: () -> Authenticator,
) : SageAppConfig {

    override fun init() {
        Api.init(
            app,
            ApiConfig(
                baseUrl,
                accessTokenInterceptor = accessTokenInterceptor,
                authenticator = accessTokenAuthenticator
            )
        )
    }

    companion object {
        const val PRODUCTION_BASE_URL = "https://api.chucknorris.io"
        const val PREPRODUCTION_BASE_URL = "https://dev.apimerchant.sageku.com"
    }
}
