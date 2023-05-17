package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.TodayPaperSoldModel
import com.vxplore.core.domain.repositoriess.TodayPaperSoldByUserIdRepository
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class TodayPaperSoldByUserIdRepositoryImpl @Inject constructor(private val myApiList: MyApiList):
    TodayPaperSoldByUserIdRepository {
    override suspend fun todayPaperSoldByUserIdRepository(dist_id: String): Resource<TodayPaperSoldModel> {
        return try {
            val  reslt = myApiList.getTodaySoldPapersByVendorId(dist_id)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }    }

}