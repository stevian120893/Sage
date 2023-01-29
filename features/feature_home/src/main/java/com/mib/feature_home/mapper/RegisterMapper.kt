package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Register
import com.mib.feature_home.dto.response.RegisterResponse

fun RegisterResponse.toDomainModel(): Register {
    return Register(
        accessToken = this.accessToken.orEmpty()
    )
}
