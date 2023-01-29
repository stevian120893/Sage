package com.mib.lib_api.dto

import com.google.gson.annotations.SerializedName

class ApiResponse<out S : Any>(

    /**
     * [data] will be non-null only if [isSuccess] is true.
     */
    @SerializedName("data")
    val data: S? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("meta")
    val meta: Meta? = null,

    @Transient override val rawData: String? = null
): ApiResponseInterface<S> {

//    val isSuccess: Boolean get() = meta?.let {
//        it.httpStatus in 200..299
//    } ?: false
}