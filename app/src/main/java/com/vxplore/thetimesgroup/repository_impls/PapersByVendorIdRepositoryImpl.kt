package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.PapersByVendorIdModel
import com.vxplore.core.domain.repositoriess.PapersByVendorIdRepository
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class PapersByVendorIdRepositoryImpl @Inject constructor(private val myApiList: MyApiList):PapersByVendorIdRepository {
    override suspend fun papersByVendorIdRepository(vendor_id: String): Resource<PapersByVendorIdModel> {
        return try {
            val  reslt = myApiList.getPapersByVendorId(vendor_id)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
}