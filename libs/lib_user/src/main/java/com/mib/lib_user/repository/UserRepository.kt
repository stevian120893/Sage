package com.mib.lib_user.repository

import com.mib.lib_user.model.UserInfo
import java.util.Date

interface UserRepository {
    suspend fun getUserInfo(): Pair<UserInfo?, String?>

//    suspend fun updateUserInfo(request: UpdateUserInfoRequest): Pair<Boolean, String?>

//    suspend fun getLastUserConsent(): Pair<Date?, String?>

//    suspend fun updateLastUserConsent(): Pair<Date?, String?>
}