package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Location
import com.mib.feature_home.dto.response.LocationResponse

fun LocationResponse.toDomainModel(): Location {
    return Location(
        code = this.code.orEmpty(),
        name = this.name.orEmpty()
    )
}
