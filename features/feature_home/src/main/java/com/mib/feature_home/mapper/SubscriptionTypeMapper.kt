package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.SubscriptionType
import com.mib.feature_home.dto.response.SubscriptionTypeResponse
import java.math.BigDecimal

fun SubscriptionTypeResponse.toDomainModel(): SubscriptionType {
    return SubscriptionType(
        subscriptionCode = this.subscriptionCode.orEmpty(),
        subscriptionName = this.subscriptionName.orEmpty(),
        subscriptionPrice = this.subscriptionPrice ?: BigDecimal.ZERO
    )
}