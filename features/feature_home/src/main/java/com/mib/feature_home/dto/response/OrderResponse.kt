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
    val orderDate: String?,
    @SerializedName("booking_date")
    val bookingDate: String?,
    @SerializedName("total_payment")
    val totalPayment: String?,
    @SerializedName("note")
    val note: String?,
)
