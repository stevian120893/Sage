package com.mib.lib_api

import com.mib.lib_api.dto.ApiResponseInterface
import com.mib.lib_api.dto.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * [R] should be of type [ApiResponseInterface]
 */
class NetworkResponseAdapter<R : Any>(
    private val successType: Type
): CallAdapter<R, Call<NetworkResponse<R>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<R>): Call<NetworkResponse<R>> = NetworkResponseCall(call)
}