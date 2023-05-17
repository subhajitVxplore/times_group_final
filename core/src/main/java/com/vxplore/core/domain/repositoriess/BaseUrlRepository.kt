package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.BaseUrlModel
import com.vxplore.core.domain.model.VendorDetailsResponse

interface BaseUrlRepository {
    suspend fun baseUrlRepository(): Resource<BaseUrlModel>
}