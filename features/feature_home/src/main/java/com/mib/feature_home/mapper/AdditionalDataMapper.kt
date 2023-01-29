package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.AdditionalData
import com.mib.feature_home.dto.response.AdditionalDataResponse

fun AdditionalDataResponse.toDomainModel(): AdditionalData {
    return AdditionalData(
        simUrl = this.sim.orEmpty(),
        stnkUrl = this.stnk.orEmpty(),
        skckUrl = this.skck.orEmpty()
    )
}
