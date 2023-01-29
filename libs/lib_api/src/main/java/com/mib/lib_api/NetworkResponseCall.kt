package com.mib.lib_api

import com.mib.lib_api.dto.ApiResponseInterface
import com.mib.lib_api.dto.NetworkResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Type [R] is cast to [ApiResponseInterface] if success
 */
class NetworkResponseCall<R : Any>(private val delegate: Call<R>) : Call<NetworkResponse<R>> {

    override fun clone(): Call<NetworkResponse<R>> = NetworkResponseCall(delegate.clone())

    override fun execute(): Response<NetworkResponse<R>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun enqueue(callback: Callback<NetworkResponse<R>>) {
        delegate.enqueue(object : Callback<R> {

            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                val body = response.body()
                when {
                    response.isSuccessful && body != null -> {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(body))
                        )
                    }
                    else -> { // Failure
                        val rawData = response.errorBody()?.string()
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResponse.Failure(rawData)
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<R>, t: Throwable) {
                val networkResponse = when (t) {
                    is UnknownHostException -> NetworkResponse.ConnectionError(t)
                    is ConnectException -> NetworkResponse.ConnectionError(t)
                    is IOException -> NetworkResponse.IOError(t)
                    else -> NetworkResponse.UnknownError(t)
                }
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(networkResponse)
                )
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}