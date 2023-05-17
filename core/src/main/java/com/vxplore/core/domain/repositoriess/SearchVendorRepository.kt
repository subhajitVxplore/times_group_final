package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.DistrictByStateModel
import com.vxplore.core.domain.model.SearchVendorModel

interface SearchVendorRepository {

    suspend fun searchVendorRepository(distributor_id: String,search_text: String): Resource<SearchVendorModel>

}