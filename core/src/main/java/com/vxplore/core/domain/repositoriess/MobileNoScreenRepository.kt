package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.SendOtpModel

interface MobileNoScreenRepository {
    suspend fun sendOtpRepository(mobile_no: String): Resource<SendOtpModel>
}