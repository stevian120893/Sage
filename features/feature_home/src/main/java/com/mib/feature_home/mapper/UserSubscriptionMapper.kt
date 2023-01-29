package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.UserSubscription
import com.mib.feature_home.dto.response.UserSubscriptionResponse
import com.mib.feature_home.utils.AppUtils

fun UserSubscriptionResponse.toDomainModel(): UserSubscription {
    return UserSubscription(
        code = this.code.orEmpty(),
        startDate = AppUtils.convertMillisToDate(this.startDate),
        endDate = AppUtils.convertMillisToDate(this.endDate),
        name = this.name.orEmpty(),
        month = this.month ?: 0
    )
}