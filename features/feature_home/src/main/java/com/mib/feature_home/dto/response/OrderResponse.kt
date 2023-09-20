package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class OrderResponse (
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
    @SerializedName("total_payment")
    val totalPayment: String?,
    @SerializedName("note")
    val note: String?,
)
