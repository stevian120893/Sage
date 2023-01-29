package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.auth.VerificationCode
import com.mib.feature_home.dto.response.VerificationCodeResponse

fun VerificationCodeResponse.toDomainModel(): VerificationCode {
    return VerificationCode(
        code = this.code.orEmpty()
    )
}