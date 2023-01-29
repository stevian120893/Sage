package com.mib.lib_api

import okhttp3.Authenticator
import okhttp3.Interceptor

class ApiConfig(
    val baseUrl: String,
    val accessTokenInterceptor: Interceptor? = null,
    val authenticator: (() -> Authenticator)? = null
)
