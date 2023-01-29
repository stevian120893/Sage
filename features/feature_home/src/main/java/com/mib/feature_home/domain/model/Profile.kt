package com.mib.feature_home.domain.model

class Profile(
    val name: String,
    val cityCode: String,
    val cityName: String,
    val profilePicture: String,
    val ktpPicture: String,
    val ktpSelfiePicture: String,
    val bankCode: String,
    val bankName: String,
    val bankAccountNumber: String,
    val isAcceptCash: Boolean,
    val isAcceptBankTransfer: Boolean,
    val referralCode: String,
    val tier: Tier
)