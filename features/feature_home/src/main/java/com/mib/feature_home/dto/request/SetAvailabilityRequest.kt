package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class SetAvailabilityRequest(
    @SerializedName("availability_days")
    val availabilityDays: List<AvailabilityDayRequest>
)
