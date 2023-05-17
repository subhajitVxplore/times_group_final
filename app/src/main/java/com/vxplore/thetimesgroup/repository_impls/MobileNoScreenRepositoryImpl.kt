package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.SendOtpModel
import com.vxplore.core.domain.repositoriess.MobileNoScreenRepository
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class MobileNoScreenRepositoryImpl @Inject constructor(private val myApiList: MyApiList):MobileNoScreenRepository {
    override suspend fun sendOtpRepository(mobile_no: String): Resource<SendOtpModel> {

            return try {
                val  reslt = myApiList.sendOtp(mobile_no)
                Resource.Success(reslt)
            } catch (ex: Exception) {
                Resource.Error(message = ex.message)
            }

    }


}