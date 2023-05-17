package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.DistributorDetailsModel
import com.vxplore.core.domain.model.VendorDetailsResponse

interface DistributorDetailsRepository {
    suspend fun distributorDetailsRepo(distributor_id: String): Resource<DistributorDetailsModel>
}