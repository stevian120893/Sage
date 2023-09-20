package com.mib.feature_home.domain.model

import androidx.annotation.StringDef
import java.math.BigDecimal

class Order(
    val code: String?,
    val address: String,
    @Status val status: String,
    val orderDate: String,
    val bookingDate: String,
    val totalPayment: BigDecimal,
    val note: String,
) {
    companion object {
        @StringDef(
            NEGOTIATING,
            WAITING_FOR_PAYMENT,
            ONGOING,
            CANCEL,
            DONE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val NEGOTIATING = "NEGOTIATING"
        const val WAITING_FOR_PAYMENT = "WAITING_FOR_PAYMENT"
        const val ONGOING = "ONGOING"
        const val CANCEL = "CANCEL"
        const val DONE = "DONE"
    }
}
