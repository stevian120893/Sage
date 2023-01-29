package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.AvailabilityDay
import com.mib.feature_home.dto.response.AvailabilityDayResponse

fun AvailabilityDayResponse.toDomainModel(): AvailabilityDay {
    return AvailabilityDay(
//        availabilityDayCode = this.availabilityDayCode.orEmpty(),
        dayName = this.dayName.orEmpty(),
        openHour = this.openHour.orEmpty(),
        closedHour = this.closedHour.orEmpty(),
        isOpen = this.isOpen ?: true
    )
}
