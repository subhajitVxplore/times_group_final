package com.vxplore.core.common

data class DialogData(
    val title: String,
    val message: String,
    val positive: String,
    val negative: String?=null,
    val neutral: String?=null,
    val data: Any?=null
)
