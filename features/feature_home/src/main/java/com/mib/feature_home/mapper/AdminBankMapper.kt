package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.AdminBank
import com.mib.feature_home.dto.response.AdminBankResponse

fun AdminBankResponse.toDomainModel(): AdminBank {
    return AdminBank(
        bankCode = this.bankCode.orEmpty(),
        bankName = this.bankName.orEmpty(),
        accountNumber = this.accountNumber.orEmpty()
    )
}
