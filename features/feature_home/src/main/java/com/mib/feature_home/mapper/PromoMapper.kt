package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.dto.response.PromoResponse
import com.mib.feature_home.utils.AppUtils
import java.math.BigDecimal

fun PromoResponse.toDomainModel(): Promo {
    return Promo(
        promoCode = this.promoCode.orEmpty(),
        promoTitle = this.promoTitle.orEmpty(),
        promoDescription = this.description.orEmpty(),
        type = this.type ?: 0,
        promoDiscountAmount = this.promoDiscountAmount ?: BigDecimal.ZERO,
        minimumTransactionAmount = this.minimumTransactionAmount ?: BigDecimal.ZERO,
        maximumDiscount = this.maximumDiscount ?: BigDecimal.ZERO,
        promoInputCode = this.promoInputCode.orEmpty(),
        promoQuota = this.promoQuota ?: 0,
        promoStartDate = AppUtils.convertMillisToDate(this.promoStartDate),
        promoExpiredDate = AppUtils.convertMillisToDate(this.promoExpiredDate),
        isTimeLimited = this.isTimeLimited ?: false,
        promoImageUrl = this.promoImageUrl.orEmpty(),
        status = this.status.orEmpty()
    )
}
