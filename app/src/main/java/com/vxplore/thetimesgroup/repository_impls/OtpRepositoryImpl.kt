package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.OtpDtailsResponse
import com.vxplore.core.domain.model.SendOtpModel
import com.vxplore.core.domain.model.VerifyOtpModel
import com.vxplore.core.domain.repositoriess.OtpRepository
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class OtpRepositoryImpl @Inject constructor(private val appVersionApi: AppVersionApi,private val myApiList: MyApiList):OtpRepository {

    override suspend fun otpDetailsRepo(myOtp: String): Resource<OtpDtailsResponse> {
        return try {
            val  reslt = appVersionApi.getOtpDetails();
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
    override suspend fun verifyOtpRepository(
        mobile_no: String,
        myOtp: String
    ): Resource<VerifyOtpModel> {
        return try {
            val  reslt = myApiList.verifyOtp(mobile_no,myOtp)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }


}