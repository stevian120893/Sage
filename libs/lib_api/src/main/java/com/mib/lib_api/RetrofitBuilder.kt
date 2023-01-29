package com.mib.lib_api

import android.os.Build
import com.mib.lib_api.converter.QueryConverterFactory
import com.mib.lib_util.DeviceUtils
import com.mib.lib_api.util.resettableLazy
import com.mib.lib_util.GsonUtils
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val CONNECT_TIMEOUT = 50L // seconds
    private const val READ_TIMEOUT = 30L // seconds
    private const val WRITE_TIMEOUT = 30L // seconds

    private val gsonConverterFactory: GsonConverterFactory = GsonConverterFactory.create(GsonUtils.GSON_WITH_UTC)
    private val queryConverterFactory: QueryConverterFactory = QueryConverterFactory.create()
    private val responseAdapterFactory: CallAdapter.Factory = NetworkResponseAdapterFactory()
    private val headerInterceptor: Interceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
//            .addHeader("User-Agent", DeviceUtils.getUserAgent())
            .addHeader("device_id", DeviceUtils.getDeviceUniqueId())
//            .addHeader("Platform", "android")
//            .addHeader("OS-Version", Build.VERSION.CODENAME)
//            .addHeader("App-Name", "BuildConfig.APP_NAME")
//            .addHeader("App-Version", "BuildConfig.VERSION_NAME")
            .build()
        chain.proceed(request)
    }

    /**
     * This must be a singleton.
     *
     * @see https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
     */
    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
        builder.build()
    }

    private val okHttpClientWithAuth: OkHttpClient by resettableLazy(Api.resettableManager) {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
        Api.apiConfig.accessTokenInterceptor?.let(builder::addInterceptor)
        Api.apiConfig.authenticator?.invoke()?.let(builder::authenticator)
        builder.build()
    }

    val retrofit: Retrofit by resettableLazy(Api.resettableManager) {
        Retrofit.Builder()
            .baseUrl(Api.apiConfig.baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(responseAdapterFactory)
            .addConverterFactory(queryConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    val retrofitWithAuth: Retrofit by resettableLazy(Api.resettableManager) {
        Retrofit.Builder()
            .baseUrl(Api.apiConfig.baseUrl)
            .client(okHttpClientWithAuth)
            .addCallAdapterFactory(responseAdapterFactory)
            .addConverterFactory(queryConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}
