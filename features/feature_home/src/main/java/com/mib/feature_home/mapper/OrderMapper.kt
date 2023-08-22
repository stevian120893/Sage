package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.dto.response.OrderResponse
import java.math.BigDecimal

fun OrderResponse.toDomainModel(): Order {
    return Order(
        code = this.code.orEmpty(),
        address = this.address.orEmpty(),
        status = this.status.orEmpty(),
        orderDate = this.orderDate.orEmpty(),
        bookingDate = this.bookingDate.orEmpty(),
        totalPayment = this.totalPayment?.toBigDecimal() ?: BigDecimal.ZERO,
        note = this.note.orEmpty(),
    )
}
