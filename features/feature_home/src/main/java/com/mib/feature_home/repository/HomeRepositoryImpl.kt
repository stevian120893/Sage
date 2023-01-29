package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.Bank
import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.domain.model.Location
import com.mib.feature_home.domain.model.auth.VerificationCode
import com.mib.feature_home.dto.request.VerificationCodeRequest
import com.mib.feature_home.mapper.toDomainModel
import com.mib.feature_home.service.HomeService
import com.mib.feature_home.utils.AppUtils
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody

class HomeRepositoryImpl(
    service: () -> HomeService
): HomeRepository {
    private val service by lazy(service)

    override suspend fun get(): Pair<Home?, String?> {
        val result = service.get()
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        location: String,
        profilePicture: MultipartBody.Part?,
        ktpPicture: MultipartBody.Part?,
        ktpSelfiePicture: MultipartBody.Part?,
        bankCode: String,
        bankAccountNumber: String,
        code: String
    ): Pair<TokenResponse?, String?> {
        val result = service.register(
            email = AppUtils.createRequestBody(email),
            password = AppUtils.createRequestBody(password),
            name = AppUtils.createRequestBody(name),
            location = AppUtils.createRequestBody(location),
            bankCode = AppUtils.createRequestBody(bankCode),
            bankAccountNumber = AppUtils.createRequestBody(bankAccountNumber),
            code = AppUtils.createRequestBody(code),
            profilePicture = profilePicture,
            ktpPicture = ktpPicture,
            ktpSelfiePicture = ktpSelfiePicture,
        )
        return when (result) {
            is NetworkResponse.Success -> result.value.data to null
            else -> null to result.getErrorMessage()
        }
    }

    override suspend fun sendCode(email: String): Pair<Void?, String?> {
        val verificationCodeRequest = VerificationCodeRequest(
            email = email
        )
        val result = service.sendCode(verificationCodeRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getLocations(): Pair<List<Location>?, String?> {
        val result = service.getLocations()
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                items to null
            }
            else -> {
                emptyList<Location>() to result.getErrorMessage()
            }
        }
    }

    override suspend fun getBanks(): Pair<List<Bank>?, String?> {
        val result = service.getBanks()
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                items to null
            }
            else -> {
                emptyList<Bank>() to result.getErrorMessage()
            }
        }
    }
}