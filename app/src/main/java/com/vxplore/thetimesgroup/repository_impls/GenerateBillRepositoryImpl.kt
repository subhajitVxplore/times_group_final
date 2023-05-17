package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.GenerateBillDataRequestModel
import com.vxplore.core.domain.model.GeneratedBillDataResponseModel
import com.vxplore.core.domain.repositoriess.GenerateBillRepository
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class GenerateBillRepositoryImpl @Inject constructor(private val myApiList: MyApiList):GenerateBillRepository {
    override suspend fun generateBillRepository(rawJson: GenerateBillDataRequestModel): Resource<GeneratedBillDataResponseModel> {
        return try {
            val  reslt = myApiList.generateBill(rawJson)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

}