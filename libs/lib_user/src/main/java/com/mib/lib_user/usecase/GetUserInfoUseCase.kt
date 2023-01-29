package com.mib.lib_user.usecase

import com.mib.lib_pref.AccountPref
import com.mib.lib_user.model.UserInfo
import com.mib.lib_user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserInfoUseCase(
    private val repo: UserRepository,
    private val accountPref: AccountPref
) {

    suspend operator fun invoke(): Pair<UserInfo?, String?> = withContext(Dispatchers.IO) {
        val result = repo.getUserInfo()
        result.first?.let(::saveDataToAccountPref)
        return@withContext result
    }

    private fun saveDataToAccountPref(userInfo: UserInfo) {
        accountPref.fullname = userInfo.fullName
        accountPref.email = userInfo.email.orEmpty()
        accountPref.phoneNumber = userInfo.phone
        accountPref.tin = userInfo.tin.orEmpty()
        accountPref.address = userInfo.address.orEmpty()
    }
}
