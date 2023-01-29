package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.BuySubscription
import com.mib.feature_home.dto.response.BuySubscriptionResponse

fun BuySubscriptionResponse.toDomainModel(): BuySubscription {
    return BuySubscription(
        code = this.code.orEmpty()
    )
}
