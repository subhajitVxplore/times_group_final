package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.AddVendorModel
import com.vxplore.core.domain.model.GenerateBillDataRequestModel
import com.vxplore.core.domain.model.GeneratedBillDataResponseModel

interface GenerateBillRepository {

    suspend fun generateBillRepository(rawJson: GenerateBillDataRequestModel): Resource<GeneratedBillDataResponseModel>
}