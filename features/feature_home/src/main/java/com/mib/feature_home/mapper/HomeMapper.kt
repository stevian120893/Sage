package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.dto.response.HomeResponse

fun HomeResponse.toDomainModel(): Home {
    return Home(
        icon = this.iconUrl.orEmpty()
    )
}
