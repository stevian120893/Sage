package com.mib.lib_api.dto

import com.mib.lib_util.GsonUtils
import com.google.gson.JsonSyntaxException
import com.mib.lib_api.ApiConstants
import java.io.IOException
import com.mib.lib_api.NetworkResponseAdapter

/**
 * This is a wrapper for [ApiResponseInterface].
 *
 * Retrofit calls must returns [NetworkResponse] and not [ApiResponseInterface] directly. This works in
 * conjunction with custom [NetworkResponseAdapter].
 */
sealed class NetworkResponse<out D: Any> {

    /**
     * Denotes that the API call is successful and [ApiResponse.data] can be accessed.
     */
    class Success<D : Any>(val value: D) : NetworkResponse<D>()

    /**
     * Denotes that the API call returns an error response. [ApiResponse.data] will return
     * [Nothing]. The developer should access [ApiResponse.meta] and parse [ApiResponse.rawData]
     * instead.
     */
    class Failure<D : Any>(rawData: String?) : NetworkResponse<D>() {
        val error: ApiError by lazy {
            try {
                val errorWrapper: ApiError = GsonUtils.fromJson(rawData)
                errorWrapper
            } catch (exception: JsonSyntaxException) {
                ApiError(ApiConstants.ERROR_MESSAGE_GENERAL)
            }
        }
    }

    /**
     * Denotes when the network requests throws error due to socket error or data parsing error.
     */
    class IOError(val error: IOException) : NetworkResponse<Nothing>()

    /**
     * Denotes when the network requests throws error due to connection issue.
     */
    class ConnectionError(val error: Throwable) : NetworkResponse<Nothing>()

    /**
     * (╯°□°）╯︵ ┻━┻
     */
    class UnknownError(val error: Throwable) : NetworkResponse<Nothing>()

    fun getErrorMessage(): String? {
        return when (this) {
            is IOError, is UnknownError -> ApiConstants.ERROR_MESSAGE_GENERAL
            is ConnectionError -> ApiConstants.ERROR_MESSAGE_NETWORK
            is Failure -> this.error.message.orEmpty()
            else -> null
        }
    }
}