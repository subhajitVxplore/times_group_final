package com.vxplore.core.domain.model

data class SendOtpModel(
    val isSent: Boolean,
    val message: String,
    val status: Boolean
)