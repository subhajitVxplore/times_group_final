package com.vxplore.core.domain.repositoriess

import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.*

interface RegisterRepository {

    suspend fun getAllStateRepository(): Resource<AllStatesModel>
    suspend fun getDistrictByStateRepository(state_id: String): Resource<DistrictByStateModel>
    suspend fun getPincodeByDistrictRepository(district: String): Resource<PincodeByDistrict>
    suspend fun registerRepository(userId: String,name: String,email: String,mobile: String,address: String,state: String,district: String,pincode: String): Resource<RegisterModel>
}