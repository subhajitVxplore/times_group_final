package com.vxplore.core.domain.model

data class PapersByVendorIdModel(
    val coupons: List<Coupon>,
    val message: String,
    val phoneNumber: String,
    val papers: List<Paper>,
    val status: Boolean,
    val dueAmount: Int
)


data class Paper(
    val key: String,
    val previous_paper_count: Int,
    val todays_price: Float,
    val previous_price: Float,
    val value: String
)

data class Coupon(
    val key: String,
    val value: Int,
    val name: String

)