package com.mib.feature_home.dto.response.order_detail

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class OrderDetailResponse (
    @SerializedName("user")
    val user: UserOrderResponse?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("order_date")
    val orderDate: Long?,
    @SerializedName("booking_date")
    val bookingDate: Long?,
    @SerializedName("order_accepted_at")
    val orderAcceptedAt: Long?,
    @SerializedName("total_price")
    val totalPrice: BigDecimal?,
    @SerializedName("discount")
    val discount: BigDecimal?,
    @SerializedName("total_payment")
    val totalPayment: BigDecimal?,
    @SerializedName("used_payment_method")
    val usedPaymentMethod: String?,
    @SerializedName("payment_receipt_image")
    val paymentReceiptImage: String?,
    @SerializedName("payment_success_at")
    val paymentSuccessAt: Long?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("detail")
    val detail: DetailResponse?,
)