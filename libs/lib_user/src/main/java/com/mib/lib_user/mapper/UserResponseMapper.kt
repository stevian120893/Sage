package com.mib.lib_user.mapper

import com.mib.lib_user.dto.response.UserInfoResponse
import com.mib.lib_user.model.UserInfo

fun UserInfoResponse.toDomainModel(): UserInfo {
    return UserInfo(
        id = id ?: 0,
        fullName = fullName,
        email = email,
        phone = phone.orEmpty(),
        businessName = businessName,
        tin = tin.orEmpty(),
        address = address.orEmpty(),
        status = status.orEmpty()
    )
}
