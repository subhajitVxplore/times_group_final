package com.vxplore.core.domain.model

data class OtpDtailsResponse(
    val message: String,
    val otp_details: List<OtpDetail>,
    val status: Boolean
)
data class OtpDetail(
    val otp: String,
    val phone: String
)