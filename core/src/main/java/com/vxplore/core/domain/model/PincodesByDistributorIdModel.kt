package com.vxplore.core.domain.model

data class PincodesByDistributorIdModel(
    val message: String,
    val pincodes: List<Pincodes>,
    val status: Boolean
)

data class Pincodes(
    val id: Int,
    val pincode: String
)