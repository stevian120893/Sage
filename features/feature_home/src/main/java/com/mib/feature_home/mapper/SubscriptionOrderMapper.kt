package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.SubscriptionOrder
import com.mib.feature_home.dto.response.SubscriptionOrderResponse
import com.mib.feature_home.utils.AppUtils

fun SubscriptionOrderResponse.toDomainModel(): SubscriptionOrder {
    return SubscriptionOrder(
        code = this.code.orEmpty(),
        startDate = AppUtils.convertMillisToDate(this.startDate),
        endDate = AppUtils.convertMillisToDate(this.endDate),
        name = this.name.orEmpty(),
        month = this.month ?: 0,
        requestDate = AppUtils.convertMillisToDate(this.requestDate),
        status = this.status.orEmpty(),
    )
}