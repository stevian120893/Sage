package com.mib.lib_user.dto.response

import com.google.gson.annotations.SerializedName
import java.util.Date

class UserInfoResponse (
    @SerializedName("id")
    val id: Long?,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("created_at")
    val createdAt: Date?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("language_tag")
    val languageTag: String?,
    @SerializedName("business_name")
    val businessName: String?,
    @SerializedName("email_id")
    val emailId: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Date?,
    @SerializedName("phone_id")
    val phoneId: Long?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("phone_verified_at")
    val phone_verified_at: Date?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("tin")
    val tin: String?,
    @SerializedName("status")
    val status: String?,
)
