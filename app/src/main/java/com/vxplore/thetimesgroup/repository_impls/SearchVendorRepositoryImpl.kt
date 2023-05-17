package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.SearchVendorModel
import com.vxplore.core.domain.repositoriess.SearchVendorRepository
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class SearchVendorRepositoryImpl @Inject constructor(private val myApiList: MyApiList):SearchVendorRepository {
    override suspend fun searchVendorRepository(
        distributor_id: String,
        search_text: String
    ): Resource<SearchVendorModel> {
        return try {
            val  reslt = myApiList.searchVendor(distributor_id,search_text)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }

    }

}