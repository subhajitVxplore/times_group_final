package com.vxplore.thetimesgroup.repository_impls

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.AddVendorModel
import com.vxplore.core.domain.model.PincodesByDistributorIdModel
import com.vxplore.core.domain.repositoriess.AddVendorRepository
import com.vxplore.thetimesgroup.mainController.MyApiList
import javax.inject.Inject

class AddVendorRepositoryImpl @Inject constructor(private val myApiList: MyApiList):AddVendorRepository {
    override suspend fun addVendorRepository(
        userId: String,
        name: String,
        mobile: String,
        email: String,
        pincodes: String
    ): Resource<AddVendorModel> {
        return try {
            val  reslt = myApiList.addVendor(userId,name,mobile,email,pincodes)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

    override suspend fun pincodesByDistributorIdRepository(uid: String): Resource<PincodesByDistributorIdModel> {
        return try {
            val  reslt = myApiList.getPincodeByDistributorId(uid)
            Resource.Success(reslt)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }
}