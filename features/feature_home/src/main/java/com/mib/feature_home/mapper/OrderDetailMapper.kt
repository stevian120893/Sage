package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.order_detail.Detail
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.domain.model.order_detail.OrderDetailProduct
import com.mib.feature_home.domain.model.order_detail.UserOrder
import com.mib.feature_home.dto.response.order_detail.OrderDetailResponse
import com.mib.feature_home.utils.AppUtils
import java.math.BigDecimal

fun OrderDetailResponse.toDomainModel(): OrderDetail {
    val user = UserOrder(
        name = this.user?.name.orEmpty(),
        email = this.user?.email.orEmpty(),
        phone = this.user?.phone.orEmpty(),
    )

    val product = OrderDetailProduct(
        code = this.detail?.product?.code.orEmpty(),
        name = this.detail?.product?.name.orEmpty()
    )

    val detail = Detail(
        product = product,
        price = this.detail?.price ?: BigDecimal.ZERO,
        qty = this.detail?.qty ?: 0,
        totalPrice = this.detail?.totalPrice ?: BigDecimal.ZERO
    )

    return OrderDetail(
        userOrder = user,
        code = this.code.orEmpty(),
        address = this.address.orEmpty(),
        status = status.orEmpty(),
        orderDate = AppUtils.convertMillisToDate(this.orderDate),
        bookingDate = AppUtils.convertMillisToDate(this.bookingDate),
        orderAcceptedAt = AppUtils.convertMillisToDate(this.orderAcceptedAt),
        totalPrice = this.totalPrice ?: BigDecimal.ZERO,
        discount = this.discount ?: BigDecimal.ZERO,
        totalPayment = this.totalPayment ?: BigDecimal.ZERO,
        usedPaymentMethod = this.usedPaymentMethod.orEmpty(),
        paymentReceiptImage = this.paymentReceiptImage.orEmpty(),
        paymentSuccessAt = AppUtils.convertMillisToDate(this.paymentSuccessAt),
        note = this.note.orEmpty(),
        detail = detail,
    )
}
