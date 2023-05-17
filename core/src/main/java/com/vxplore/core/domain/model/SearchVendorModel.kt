package com.vxplore.core.domain.model

data class SearchVendorModel(
    val message: String,
    val status: Boolean,
    val vendors: List<SearchVendor>? = null
)

data class SearchVendor(
    val name: String,
    val user_id: String
)