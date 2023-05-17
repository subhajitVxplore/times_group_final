package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.PapersByVendorIdModel

interface PapersByVendorIdRepository {

    suspend fun papersByVendorIdRepository(vendor_id: String):Resource<PapersByVendorIdModel>

}