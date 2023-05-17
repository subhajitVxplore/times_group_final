package com.vxplore.core.domain.model

import com.vxplore.core.common.Action

//@Serializable
data class SortParameter(
    val id: String,
    val name: String
)


data class Command(
    val actionType: Action,
    val targetType: Any?
)