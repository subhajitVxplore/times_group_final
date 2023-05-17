package com.vxplore.thetimesgroup.data.online

import com.vxplore.core.domain.model.DonutChartModel
import com.vxplore.core.domain.model.OtpDtailsResponse
import com.vxplore.core.domain.model.RegisterDetailsResponse
import com.vxplore.core.domain.model.VendorDetailsResponse
import retrofit2.http.GET

interface AppVersionApi {


//    @GET("dbc734947dc2b6deb690")
//    suspend fun getAppVersion(): AppVersionResponse

    @GET("e3bddd858343a2b4aa3c")
    suspend fun getRegisterDetails(): RegisterDetailsResponse

    @GET("167262d8a7b75af21497")
    suspend fun getOtpDetails(): OtpDtailsResponse

    @GET("827a5ea26aa944fcd1d2")
    suspend fun getVendorDetails(): VendorDetailsResponse

    @GET("990c5926c3c2c14a0342")
    suspend fun getDonutChartDetails(): DonutChartModel


}