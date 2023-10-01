package com.mib.feature_home.domain.model.order_detail

import androidx.annotation.StringDef
import com.mib.feature_home.domain.model.PaymentMethod
import java.math.BigDecimal

class OrderDetail (
    val userOrder: UserOrder,
    val code: String,
    val address: String,
    @Status val status: String,
    val orderDate: String,
    val bookingDate: String,
    val bookingTime: String,
    val orderAcceptedAt: String,
    val totalPrice: BigDecimal,
    val discount: BigDecimal,
    val totalPayment: BigDecimal,
    val usedPaymentMethod: String,
    val paymentReceiptImage: String,
    val paymentSuccessAt: String,
    val note: String,
    val detail: Detail,
) {
    companion object {
        @StringDef(
            NEGOTIATING,
            WAITING_FOR_PAYMENT,
            ONGOING,
            CANCEL,
            DONE,
            UNKNOWN
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val NEGOTIATING = "NEGOTIATING"
        const val WAITING_FOR_PAYMENT = "WAITING_FOR_PAYMENT"
        const val ONGOING = "ONGOING"
        const val CANCEL = "CANCEL"
        const val DONE = "DONE"
        const val UNKNOWN = "UNKNOWN"
    }
}
