package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.PapersByVendorIdModel
import com.vxplore.core.domain.model.TodayPaperSoldModel

interface TodayPaperSoldByUserIdRepository {
    suspend fun todayPaperSoldByUserIdRepository(dist_id: String): Resource<TodayPaperSoldModel>
}