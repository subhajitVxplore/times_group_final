package com.vxplore.core.domain.model

data class GeneratedBillDataResponseModel(
//    val isAdded: Boolean,
//    val message: String,
//    val pdfData: String,
//    val status: Boolean

    val assigned_papers: List<AssignedPaper>,
    val billDetails: BillDetails,
    val billId: String,
    val coupons: List<Couponn>,
    val date: String,
    val isAdded: Boolean,
    val message: String,
    val pdfUrl: String,
    val returned_papers: List<ReturnedPaper>,
    val status: Boolean,
    val vendorName: String
)
data class AssignedPaper(
    val name: String,
    val price: String,
    val qty: String,
    val total: String
)
data class BillDetails(
    val calculated_price: String,
    val coupon_sub_total: String,
    val currrent_due_amount: String,
    val old_due_amount: String,
    val order_sub_total: String,
    val payment_by_cash: String,
    val return_sub_total: String
)

data class Couponn(
    val name: String,
    val price: String,
    val qty: String,
    val total: String
)

data class ReturnedPaper(
    val name: String,
    val price: String,
    val qty: String,
    val total: String
)

