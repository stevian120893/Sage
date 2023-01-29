package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Bank
import com.mib.feature_home.dto.response.BankResponse

fun BankResponse.toDomainModel(): Bank {
    return Bank(
        code = this.code.orEmpty(),
        name = this.name.orEmpty()
    )
}
