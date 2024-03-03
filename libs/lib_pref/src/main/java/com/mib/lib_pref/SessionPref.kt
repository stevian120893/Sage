package com.mib.lib_pref

import android.content.SharedPreferences
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.mib.lib_pref.file.AppBoundPref
import com.mib.lib_pref.file.UserBoundPref
import java.util.Date

/**
 * Shared preference to save data related to users' session and does not need to be encrypted
 * If the data need to be encrypted, please use [SecureSessionPref] instead
 */
class SessionPref(
    appBoundPref: () -> SharedPreferences,
    userBoundPref: () -> SharedPreferences
) {
    private val _appBoundPref by lazy(appBoundPref)
    private val _userBoundPref by lazy(userBoundPref)

    @SessionType
    var sessionType by _userBoundPref.string(key = SESSION_TYPE)

    val isLoggedIn: Boolean get() = sessionType == USER

    var userId: Long by _userBoundPref.long(key = USER_ID)

    /**
     * Base36 string of current user id, used for tracker
     */
    val userIdBase36: String get() = if (userId > 0) userId.toString(36) else ""

    var journeyType: Int by _appBoundPref.int(key = JOURNEY_TYPE, defaultValue = JOURNEY_NOT_STARTED)

    var fcmToken: String by _userBoundPref.string(key = FCM_TOKEN)

    /**
     * Journey id of user, used in tracker
     * won't be cleared when the user logs out
     */
    private var _journeyId: String by _appBoundPref.string(key = JOURNEY_ID)

    val journeyId: String = _journeyId

    /**
     * Call this once when Application starts and before tracker events are sent
     */
    fun generateJourneyId() {
        _journeyId = StringBuilder()
            .append(userId.toString())
            .append("-")
            .append(Date().time)
            .toString()
    }

    fun isFirstJourney(): Boolean = journeyType == JOURNEY_FIRST

    @StringDef(LIMITED, USER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class SessionType

    @IntDef(JOURNEY_NOT_STARTED, JOURNEY_FIRST, JOURNEY_STARTED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class JourneyType

    companion object {
        private const val JOURNEY_ID = "journeyId"
        private const val SESSION_TYPE = "sessionType"
        private const val USER_ID = "userId"
        private const val JOURNEY_TYPE = "journeyType"
        private const val FCM_TOKEN = "fcmToken"

        const val JOURNEY_NOT_STARTED = -1
        const val JOURNEY_FIRST = 0
        const val JOURNEY_STARTED = 1

        const val LIMITED = "limited"
        const val USER = "user"

        fun get(): SessionPref {
            return SessionPref(AppBoundPref::create, UserBoundPref::create)
        }
    }
}