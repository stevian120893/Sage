package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class ApproveOrderRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("note")
    val note: String
)
