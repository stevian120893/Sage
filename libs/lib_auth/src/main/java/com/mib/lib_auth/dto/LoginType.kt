package com.mib.lib_auth.dto

import androidx.annotation.StringDef
import com.mib.lib_auth.dto.LoginTypes.PHONE_OTP

@StringDef(PHONE_OTP)
@Retention(AnnotationRetention.SOURCE)
annotation class LoginType

object LoginTypes {
    const val PHONE_OTP = "phone_otp"
}