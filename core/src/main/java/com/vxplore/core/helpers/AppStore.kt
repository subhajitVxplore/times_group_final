package com.vxplore.core.helpers

interface AppStore {

    suspend fun intro(status: Boolean)
    suspend fun isIntroDone(): Boolean
    suspend fun storeBaseUrl(url: String)
    suspend fun fetchBaseUrl(): String
    suspend fun storeRegistrationStatus(userStatus: String)
    suspend fun fetchRegistrationStatus(): String
    suspend fun login(userId:String,)
    suspend fun userId(): String
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun isRegistered(): Boolean
    suspend fun storeFCMToken(token: String)
    suspend fun lastFCMToken(): String
    suspend fun removeLastFCMToken(): Boolean

    suspend fun savePrinter(macAddress: String)
    suspend fun printer(): String?


}

