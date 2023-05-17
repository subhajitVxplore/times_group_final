package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.VendorDetailsResponse

interface VendorDetailsRepository {
    suspend fun vendorDetailsRepo(dist_id: String): Resource<VendorDetailsResponse>
}