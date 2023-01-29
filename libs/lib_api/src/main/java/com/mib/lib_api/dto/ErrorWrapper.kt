package com.mib.lib_api.dto

import com.google.gson.annotations.SerializedName

internal class ErrorWrapper<out E : ApiError>(
    @SerializedName("errors")
    val errors: List<E>
)