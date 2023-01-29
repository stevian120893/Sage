package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.domain.model.Tier
import com.mib.feature_home.dto.response.ProfileResponse

fun ProfileResponse.toDomainModel(): Profile {
    return Profile(
        name = this.name.orEmpty(),
        cityCode = this.cityCode.orEmpty(),
        cityName = this.cityName.orEmpty(),
        profilePicture = this.profilePicture.orEmpty(),
        ktpPicture = this.ktpPicture.orEmpty(),
        ktpSelfiePicture = this.selfiePicture.orEmpty(),
        bankCode = this.bankCode.orEmpty(),
        bankName = this.bankName.orEmpty(),
        bankAccountNumber = this.bankAccountNumber.orEmpty(),
        isAcceptCash = this.isAcceptCash ?: false,
        isAcceptBankTransfer = this.isAcceptBankTransfer ?: false,
        referralCode = this.referralCode.orEmpty(),
        tier = Tier(
            this.tier?.currentTier.orEmpty(),
            this.tier?.imageTier.orEmpty(),
            this.tier?.referralAmount.orEmpty(),
            this.tier?.nextTier.orEmpty()
        )
    )
}
