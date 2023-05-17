package com.vxplore.core.domain.model

import com.vxplore.core.common.DropDownPincodeInterface

data class PincodeByDistrict(
    val message: String,
    val pincodes: List<Pincode>,
    val status: Boolean
)

data class Pincode(
    val id: Int,
    override val pincode: String
) : DropDownPincodeInterface