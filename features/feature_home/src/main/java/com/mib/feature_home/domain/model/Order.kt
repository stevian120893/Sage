package com.mib.feature_home.domain.model

import java.math.BigDecimal

class Order(
    val code: String?,
    val address: String,
    val status: String,
    val orderDate: String,
    val bookingDate: String,
    val totalPayment: BigDecimal,
    val note: String,
)