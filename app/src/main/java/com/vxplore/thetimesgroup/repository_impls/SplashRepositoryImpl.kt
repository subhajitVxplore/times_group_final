package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.AppVersionModel
import com.vxplore.core.domain.model.BaseUrlModel
import com.vxplore.core.domain.repositoriess.SplashRepository
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
//        private val appVersionApi: AppVersionApi,
        private val myApiList: MyApiList
): SplashRepository {
    override suspend fun appVersion(currentVersion: Int): Resource<AppVersionModel> {
       return try {
            val  reslt = myApiList.getAppVersion();
            Resource.Success(reslt)
        } catch (ex: Exception) {
           Resource.Error(message = ex.message)
        }

        /*delay(2000L)
        return Resource.Success(
            AppVersionResponse(
                status = true,
                message = "Success",
                appVersion = AppVersion(
                    versionCode = 1,
                    releaseDate = "11/22/2022",
                    versionMessage = "This version is outdated",
                    isSkipable = false,
                    link = "asdsad"
                )
            )
        )*/
    }

    override suspend fun baseUrl(): Resource<BaseUrlModel> {
        return try {
            val  reslt = myApiList.getBaseUrl()
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
}