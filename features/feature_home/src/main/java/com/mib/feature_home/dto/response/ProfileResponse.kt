package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class ProfileResponse (
    @SerializedName("name")
    val name: String?,
    @SerializedName("city_code")
    val cityCode: String?,
    @SerializedName("city_name")
    val cityName: String?,
    @SerializedName("profile_picture")
    val profilePicture: String?,
    @SerializedName("ktp_picture")
    val ktpPicture: String?,
    @SerializedName("selfie_picture")
    val selfiePicture: String?,
    @SerializedName("bank_code")
    val bankCode: String?,
    @SerializedName("bank_name")
    val bankName: String?,
    @SerializedName("bank_account_number")
    val bankAccountNumber: String?,
    @SerializedName("is_accept_cash")
    val isAcceptCash: Boolean?,
    @SerializedName("is_accept_bank_transfer")
    val isAcceptBankTransfer: Boolean?,
    @SerializedName("referral_code")
    val referralCode: String?,
    @SerializedName("tier")
    val tier: TierResponse?

)
