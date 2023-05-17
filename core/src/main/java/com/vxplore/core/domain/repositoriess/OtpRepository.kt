package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.OtpDtailsResponse
import com.vxplore.core.domain.model.SendOtpModel
import com.vxplore.core.domain.model.VerifyOtpModel

interface OtpRepository {
    suspend fun otpDetailsRepo(myOtp: String): Resource<OtpDtailsResponse>
    suspend fun verifyOtpRepository(mobile_no: String,myOtp: String): Resource<VerifyOtpModel>

}