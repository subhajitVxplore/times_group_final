package com.vxplore.thetimesgroup.helpers_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vxplore.core.common.PrefConstants
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppStoreImpl @Inject constructor(
//    private val prefs: DataStore<androidx.datastore.preferences.core.Preferences>
    private val prefs: DataStore<androidx.datastore.preferences.core.Preferences>
) : AppStore {
    override suspend fun intro(status: Boolean) {
        prefs.edit {
            it[booleanPreferencesKey(PrefConstants.INTRO_STATUS)] = status
        }
    }

    override suspend fun isIntroDone(): Boolean {
        return prefs.data.map {
            it[booleanPreferencesKey(PrefConstants.INTRO_STATUS)]
        }.first() ?: false
    }

    override suspend fun storeBaseUrl(url: String) {
        prefs.edit {
            it[stringPreferencesKey(PrefConstants.BASE_URL)] = url
        }
    }

    override suspend fun fetchBaseUrl(): String {
        return prefs.data.map {
            it[stringPreferencesKey(PrefConstants.BASE_URL)]
        }.first() ?: ""
    }
    override suspend fun storeRegistrationStatus(userStatus: String) {
        prefs.edit {
            it[stringPreferencesKey(PrefConstants.REGISTRATION_STATUS)] = userStatus
        }
    }

    override suspend fun fetchRegistrationStatus(): String {
        return prefs.data.map {
            it[stringPreferencesKey(PrefConstants.REGISTRATION_STATUS)]
        }.first() ?: ""
    }
    override suspend fun login(userId: String) {
        prefs.edit {
            it[stringPreferencesKey(PrefConstants.USER_ID)] = userId
        }
    }

    override suspend fun userId(): String {
        return prefs.data.map {
            it[stringPreferencesKey(PrefConstants.USER_ID)]
        }.first() ?: ""
    }

    override suspend fun logout() {
        prefs.edit {
            if (it.contains(stringPreferencesKey(PrefConstants.USER_ID))) {
                it.remove(stringPreferencesKey(PrefConstants.USER_ID))
            }
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        val userID = prefs.data.map {
            it[stringPreferencesKey(PrefConstants.USER_ID)]
        }.first()

        if (userID != null && userID.isNotEmpty()) return true

        return false
    }

    override suspend fun isRegistered(): Boolean {
        val otp = prefs.data.map {
            it[stringPreferencesKey(PrefConstants.OTP)]
        }.first()

        if (otp != null && otp.isNotEmpty()) return true

        return false
    }

    override suspend fun storeFCMToken(token: String) {
        prefs.edit {
            it[stringPreferencesKey(PrefConstants.FCM_TOKEN)] = token
        }
    }

    override suspend fun lastFCMToken(): String {
        return prefs.data.map {
            it[stringPreferencesKey(PrefConstants.FCM_TOKEN)]
        }.first() ?: ""
    }

    override suspend fun removeLastFCMToken(): Boolean {
        return prefs.edit {
            it.remove(stringPreferencesKey(PrefConstants.FCM_TOKEN))
        }.contains(stringPreferencesKey(PrefConstants.FCM_TOKEN))
    }

    override suspend fun savePrinter(macAddress: String) {
        prefs.edit {
            it[stringPreferencesKey(PrefConstants.PRINTER_ID)] = macAddress
        }
    }

    override suspend fun printer(): String? {
        return prefs.data.map {
            it[stringPreferencesKey(PrefConstants.PRINTER_ID)]
        }.first()
    }

}