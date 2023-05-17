package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.BaseUrlModel
import com.vxplore.core.domain.repositoriess.BaseUrlRepository
import com.vxplore.core.domain.repositoriess.VendorDetailsRepository
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class BaseUrlRepositoryImpl @Inject constructor(private val myApiList: MyApiList) :BaseUrlRepository {
    override suspend fun baseUrlRepository(): Resource<BaseUrlModel> {
        return try {
            val reslt = myApiList.getBaseUrl()
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

}