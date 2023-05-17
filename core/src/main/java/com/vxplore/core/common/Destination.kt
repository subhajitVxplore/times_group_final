package com.vxplore.core.common

import android.util.Log

sealed class Destination(protected val route: String, vararg arguments: Any) {

    val fullRoute: String = if (arguments.isEmpty()) route else {
        val builder = StringBuilder(route)
        arguments.forEach { builder.append("/{${it}}") }
        Log.e("FullRoute", builder.toString())
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    object Splash : NoArgumentsDestination(AppRoutes.SPLASH)
    object MobileNo : Destination(AppRoutes.MOBILE_NO, "mobileNo_id") {
        const val MOBILE_N = "mobileNo_id"
        const val DUMMY_NUMBER = "0000000000"
        operator fun invoke(number: String = DUMMY_NUMBER): String = route.appendParams(
            MOBILE_N to number
        )
    }

    object Otp : Destination(route = AppRoutes.OTP, "mobileNo_id") {
        const val MOBILE_NO = "mobileNo_id"

        operator fun invoke(number: String): String = route.appendParams(
            MOBILE_NO to number
        )
    }

    // object Otp : NoArgumentsDestination(AppRoutes.OTP )
    object Dashboard : NoArgumentsDestination(AppRoutes.DASHBOARD)

    //    object Register : NoArgumentsDestination(AppRoutes.REGISTER)
    object Register : Destination(route = AppRoutes.REGISTER, "mobileNo_id") {
        const val MOBILE_NO = "mobileNo_id"
        operator fun invoke(number: String): String = route.appendParams(
            MOBILE_NO to number
        )
    }

    object Billing : NoArgumentsDestination(AppRoutes.BILLING)
    object BillPreview : NoArgumentsDestination(AppRoutes.BILL_PREVIEW)
    object AddVendor : NoArgumentsDestination(AppRoutes.ADD_VENDOR)
//    object AddVendor : Destination(AppRoutes.ADD_VENDOR,"added_vendor_id"){
//        const val ADDED_VENDOR = "added_vendor_id"
//        operator fun invoke(vendor: String): String = route.appendParams(
//            ADDED_VENDOR to vendor
//        )
//    }
    object AddVendorSuccess : Destination(AppRoutes.ADD_VENDOR_SUCCESS, "added_vendor_id") {
        const val ADDED_VENDORR = "added_vendor_id"
        operator fun invoke(vendor: String): String = route.appendParams(
            ADDED_VENDORR to vendor
        )

    }
}

private fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            if (arg.isNotEmpty()) {
                builder.append("/$arg")
            }

        }
    }

    return builder.toString()
}