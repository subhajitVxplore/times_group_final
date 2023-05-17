package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.AddVendorModel
import com.vxplore.core.domain.model.PincodesByDistributorIdModel

interface AddVendorRepository {

    suspend fun addVendorRepository(userId: String,name: String,mobile: String,email: String,pincodes: String): Resource<AddVendorModel>
    suspend fun pincodesByDistributorIdRepository(uid: String): Resource<PincodesByDistributorIdModel>
}