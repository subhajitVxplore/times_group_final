package com.vxplore.thetimesgroup.mainController

import com.vxplore.core.domain.model.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface MyApiList {

    @GET("baseUrl")
    suspend fun getBaseUrl(): BaseUrlModel

    @GET("appVersion")
    suspend fun getAppVersion(): AppVersionModel

    @GET("states")
    suspend fun getAllState(): AllStatesModel

    @GET("state/districts")
    suspend fun getDistrictByState(@Query("state") state: String): DistrictByStateModel

    @GET("state/districts/pincodes")
    suspend fun getPincodeByDistrict(@Query("district") district: String): PincodeByDistrict

    @FormUrlEncoded
    @POST("users/{userId}/registration")
    suspend fun register(
        @Path("userId") userId: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("address") address: String,
        @Field("state") state: String,
        @Field("district") district: String,
        @Field("pincode") pincode: String
    ): RegisterModel

    @GET("sendOtp")
    suspend fun sendOtp(@Query("mobile") mobile: String): SendOtpModel

    @GET("verifyOtp")
    suspend fun verifyOtp(
        @Query("mobile") mobile: String,
        @Query("otp") otp: String
    ): VerifyOtpModel

    @FormUrlEncoded
    @POST("users/{userId}/add/vendor")
    suspend fun addVendor(
        @Path("userId") userId: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("pincodes") pincodes: String
    ): AddVendorModel

    @GET("vendor/search")
    suspend fun searchVendor(
        @Query("distributor_id") distributor_id: String,
        @Query("search_text") search_text: String
    ): SearchVendorModel

    @GET("pincodes/distributor")
    suspend fun getPincodeByDistributorId(@Query("uid") uid: String): PincodesByDistributorIdModel

    @GET("billings")
    suspend fun getPapersByVendorId(@Query("vendor_id") vendor_id: String): PapersByVendorIdModel

    @GET("distributor/details")
    suspend fun getDistributorDetails(@Query("distributor_id") distributor_id: String): DistributorDetailsModel

    @GET("dashboard/papers")
    suspend fun getTodaySoldPapersByVendorId(@Query("dist_id") dist_id: String): TodayPaperSoldModel

    @GET("dashboard/papers/chart")
    suspend fun getDonutChartDetails(@Query("dist_id") dist_id: String): DonutChartModel

    @GET("dashboard/list/vendor")
    suspend fun getVendorDetails(@Query("dist_id") dist_id: String): VendorDetailsResponse

    @POST("Api/Billings/genarate_Bill")
    suspend fun generateBill(@Body rawJson: GenerateBillDataRequestModel):GeneratedBillDataResponseModel

//    @POST("prefix/user/{login}")
//    fun login(@Path("login") postfix: String?, @Body params: RequestBody?): Call<ResponseBody?>?



}