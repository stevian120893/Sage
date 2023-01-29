package com.mib.lib_api

import com.mib.lib_api.dto.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<Instance of ApiResponseInterface>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)

        // if the response type is not NetworkResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }

        // check first that the response type is `ParameterizedType`
        check(responseType is ParameterizedType) {
            "return type must be parameterized as NetworkResponse<Instance of ApiResponseInterface>"
        }

        // get the data type inside the `NetworkResponse` type
        val successType = getParameterUpperBound(0, responseType)

        return NetworkResponseAdapter<Any>(successType)
    }
}