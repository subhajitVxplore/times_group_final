package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.DistributorDetailsModel
import com.vxplore.core.domain.model.VendorDetailsResponse
import com.vxplore.core.domain.repositoriess.DistributorDetailsRepository
import com.vxplore.core.domain.repositoriess.VendorDetailsRepository
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class DistributorDetailsRepositoryImpl @Inject constructor(private val myApiList: MyApiList) :DistributorDetailsRepository {


    override suspend fun distributorDetailsRepo(distributor_id: String): Resource<DistributorDetailsModel> {
        return try {
            val reslt = myApiList.getDistributorDetails(distributor_id)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
}