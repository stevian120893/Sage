package com.mib.lib_api.dto

import com.google.gson.annotations.SerializedName

class OffsetPaginationMeta(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("total")
    val total: Int
)