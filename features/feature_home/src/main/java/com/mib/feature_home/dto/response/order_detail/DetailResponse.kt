package com.mib.feature_home.dto.response.order_detail

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class DetailResponse (
    @SerializedName("product")
    val product: ProductResponse?,
    @SerializedName("price")
    val price: BigDecimal?,
    @SerializedName("qty")
    val qty: Int?,
    @SerializedName("total_price")
    val totalPrice: BigDecimal?
)
