package com.mib.lib_api

object ApiConstants {

    val ERROR_MESSAGE_GENERAL get() = Api.context.getString(R.string.api_general_error)
    val ERROR_MESSAGE_NETWORK get() = Api.context.getString(R.string.api_connection_error)
    val ERROR_MESSAGE_MAINTENANCE get() = Api.context.getString(R.string.api_maintenance_error)
    // this error message comes from the API
    // TODO: use code instead of message for this error
    val ERROR_MESSAGE_UNAUTHORIZED get() = Api.context.getString(R.string.api_user_unauthorized)
    val ERROR_MESSAGE_FROZEN_ACCOUNT get() = Api.context.getString(R.string.api_user_frozen_error)
    val ERROR_MESSAGE_WITHDRAWAL_NOT_FOUND get() = Api.context.getString(R.string.api_withdrawal_not_found_error)
    val ERROR_MESSAGE_REFERRAL_CODE_INVALID get() = Api.context.getString(R.string.api_referral_code_invalid_error)

    // region voucher
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND get() = Api.context.getString(R.string.api_voucher_not_found_error)
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND_OUT_OF_QUOTA get() = Api.context.getString(R.string.api_voucher_not_found_out_of_quota_error)
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND_USAGE_REACH_LIMIT get() = Api.context.getString(R.string.api_voucher_not_found_usage_reach_limit_error)
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND_DAILY_USAGE_REACH_LIMIT get() = Api.context.getString(R.string.api_voucher_not_found_daily_usage_reach_limit_error)
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND_NOT_PRESENT get() = Api.context.getString(R.string.api_voucher_not_found_not_present_error)
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND_CAMPAIGN_NOT_STARTED get() = Api.context.getString(R.string.api_voucher_not_found_campaign_not_started_error)
    val ERROR_MESSAGE_VOUCHER_NOT_FOUND_CAMPAIGN_ENDED get() = Api.context.getString(R.string.api_voucher_not_found_campaign_ended_error)

    val ERROR_MESSAGE_VOUCHER_NOT_ELIGIBLE_TRANSACTION_AMOUNT_NOT_ENOUGH get() = Api.context.getString(R.string.api_voucher_not_eligible_transaction_amount_not_enough_error)
    val ERROR_MESSAGE_VOUCHER_NOT_ELIGIBLE_INVALID_CATEGORY_GENERAL get() = Api.context.getString(R.string.api_voucher_not_eligible_invalid_category_general_error)
    // end region
}
