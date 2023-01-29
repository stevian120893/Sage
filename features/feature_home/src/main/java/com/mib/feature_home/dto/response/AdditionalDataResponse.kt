package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class AdditionalDataResponse (
    @SerializedName("sim")
    val sim: String?,
    @SerializedName("stnk")
    val stnk: String?,
    @SerializedName("skck")
    val skck: String?
)
