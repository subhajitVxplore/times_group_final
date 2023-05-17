package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.VendorDetailsResponse
import com.vxplore.core.domain.repositoriess.VendorDetailsRepository
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class VendorDetailsRepositoryImpl @Inject constructor(private val myApiList: MyApiList) :VendorDetailsRepository {
    override suspend fun vendorDetailsRepo(dist_id: String): Resource<VendorDetailsResponse> {
        return try {
            val reslt = myApiList.getVendorDetails(dist_id)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
}