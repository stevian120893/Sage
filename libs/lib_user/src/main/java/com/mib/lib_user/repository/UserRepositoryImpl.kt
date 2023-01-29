package com.mib.lib_user.repository

import com.mib.lib_user.mapper.toDomainModel
import com.mib.lib_user.model.UserInfo
import com.mib.lib_user.service.UserAuthenticatedService
import com.mib.lib_api.dto.NetworkResponse

class UserRepositoryImpl(userAuthenticatedService: () -> UserAuthenticatedService) :
    UserRepository {
    private val userAuthenticatedService by lazy(userAuthenticatedService)

    override suspend fun getUserInfo(): Pair<UserInfo?, String?> {
        return when (val result = userAuthenticatedService.getUserInfo()) {
            is NetworkResponse.Success -> result.value.data?.toDomainModel() to null
            else -> null to result.getErrorMessage()
        }
    }

//    override suspend fun updateUserInfo(request: UpdateUserInfoRequest): Pair<Boolean, String?> {
//        return when (val result = userAuthenticatedService.updateUserInfo(request)) {
//            is NetworkResponse.Success -> true to null
//            else -> false to result.getErrorMessage()
//        }
//    }

//    override suspend fun getLastUserConsent(): Pair<Date?, String?> {
//        return when (val result = userAuthenticatedService.getLastUserConsent()) {
//            is NetworkResponse.Success -> result.value.data?.lastConsentDate to null
//            else -> null to result.getErrorMessage()
//        }
//    }
//
//    override suspend fun updateLastUserConsent(): Pair<Date?, String?> {
//        return when (val result = userAuthenticatedService.updateLastUserConsent()) {
//            is NetworkResponse.Success -> result.value.data?.lastConsentDate to null
//            else -> null to result.getErrorMessage()
//        }
//    }
}
