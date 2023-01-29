package com.mib.lib_api.dto

import com.google.gson.annotations.SerializedName

class Meta(
//    @SerializedName("http_status")
//    val httpStatus: Int,
    @SerializedName("next_cursor")
    val nextCursor: String?,
//    @SerializedName("offset_pagination")
//    val offsetPagination: OffsetPaginationMeta?
)