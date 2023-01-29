package com.mib.lib_pref

import android.content.SharedPreferences
import com.mib.lib_pref.file.UserBoundPref

/**
 * Shared preference to save data related to user's account information
 */
class AccountPref(
    sharedPref: () -> SharedPreferences
) {

    private val _sharedPref by lazy(sharedPref)

    var fullname by _sharedPref.string(key = FULLNAME)

    var phoneNumber by _sharedPref.string(key = PHONE_NUMBER)

    var email by _sharedPref.string(key = EMAIL)

    var kycStatus by _sharedPref.string(key = KYC_STATUS)

    var shouldShowVerificationSheet by _sharedPref.boolean(key = SHOULD_SHOW_VERIFICATION_SHEET, defaultValue = true)

    var tin by _sharedPref.string(key = TIN)

    var address by _sharedPref.string(key = ADDRESS)

    // TODO: move wallet related pref to wallet module
    var walletBalance by _sharedPref.string(key = WALLET_BALANCE)

    var walletBalanceLastFetch by _sharedPref.long(key = WALLET_BALANCE_LAST_FETCH, defaultValue = 0L)

    var shouldShowWithdrawTnC by _sharedPref.boolean(key = SHOULD_SHOW_WITHDRAW_TNC, defaultValue = true)

    var shouldShowTopupLimitOnboarding by _sharedPref.boolean(key = SHOULD_SHOW_TOPUP_LIMIT_ONBOARDING, defaultValue = true)

    companion object {
        private const val FULLNAME = "fullname"
        private const val PHONE_NUMBER = "phoneNumber"
        private const val EMAIL = "email"
        private const val KYC_STATUS = "kycStatus"
        private const val SHOULD_SHOW_VERIFICATION_SHEET = "shouldShowVerificationSheet"
        private const val TIN = "tin"
        private const val ADDRESS = "address"
        private const val WALLET_BALANCE = "walletBalance"
        private const val WALLET_BALANCE_LAST_FETCH = "walletBalanceLastFetch"
        private const val SHOULD_SHOW_WITHDRAW_TNC = "shouldShowWithdrawTnC"
        private const val SHOULD_SHOW_TOPUP_LIMIT_ONBOARDING = "shouldShowTopupLimitOnboarding"

        fun get(): AccountPref {
            return AccountPref(UserBoundPref::create)
        }
    }
}
