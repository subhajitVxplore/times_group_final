package com.vxplore.core.domain.model

data class VendorDetailsResponse(
    val message: String,
    val status: Boolean,
    val top_vendors: List<Vendor>
)

data class Vendor(
    val daily_avg: String,
    val name: String,
    val payment_due: String,
    val return_avg: String
)