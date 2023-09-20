package com.mib.feature_home.utils

import android.content.Context
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.Order

object CustomUtils {
    fun getUserFriendlyOrderStatusName(
        context: Context,
        status: String
    ): String {
        return when (status) {
            Order.NEGOTIATING -> context.getString(R.string.shared_res_status_negotiating)
            Order.WAITING_FOR_PAYMENT -> context.getString(R.string.shared_res_status_waiting_for_payment)
            Order.ONGOING -> context.getString(R.string.shared_res_status_ongoing)
            Order.CANCEL -> context.getString(R.string.shared_res_status_cancel)
            Order.DONE -> context.getString(R.string.shared_res_status_done)
            else -> context.getString(R.string.shared_res_status_unknown)
        }
    }
}