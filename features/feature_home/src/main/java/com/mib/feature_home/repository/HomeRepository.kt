package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.Bank
import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.domain.model.Location
import com.mib.feature_home.domain.model.Register
import com.mib.feature_home.domain.model.auth.VerificationCode
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody

interface HomeRepository {
    suspend fun get(): Pair<Home?, String?>
    suspend fun register(
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
    ): Pair<TokenResponse?, String?>
    suspend fun sendCode(
        email: String
    ): Pair<Void?, String?>
    suspend fun getLocations(): Pair<List<Location>?, String?>
    suspend fun getBanks(): Pair<List<Bank>?, String?>
}