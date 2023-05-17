package com.vxplore.core.domain.model

data class RegisterDetailsResponse(
    val message: String,
    val register_details: List<RegisterDetail>,
    val status: Boolean
)

data class RegisterDetail(
    val address: String,
    val district: String,
    val email: String,
    val mobile_no: String,
    val pincode: String,
    val state: String
)