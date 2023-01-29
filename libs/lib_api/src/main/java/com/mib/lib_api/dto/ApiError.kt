package com.mib.lib_api.dto

import com.google.gson.annotations.SerializedName

open class ApiError(
    @SerializedName("message")
    val message: String?,
//    @SerializedName("code")
//    val code: String?,
//    @SerializedName("field")
//    val field: String?
)