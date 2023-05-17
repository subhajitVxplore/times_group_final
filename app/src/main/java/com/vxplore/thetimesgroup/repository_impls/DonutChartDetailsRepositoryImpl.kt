package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.DonutChartModel
import com.vxplore.core.domain.repositoriess.DonutChartDetailsRepository
import com.vxplore.core.domain.repositoriess.VendorDetailsRepository
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class DonutChartDetailsRepositoryImpl @Inject constructor(private val myApiList: MyApiList) :
    DonutChartDetailsRepository {
    override suspend fun donutChartDetailsRepository(dist_id: String): Resource<DonutChartModel> {
        return try {
            val reslt = myApiList.getDonutChartDetails(dist_id)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
}