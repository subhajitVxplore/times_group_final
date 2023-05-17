package com.vxplore.core.domain.model

import com.vxplore.core.common.DropDownCommonInterface

data class DistrictByStateModel(
    val districts: List<District>,
    val message: String,
    val status: Boolean
)

data class District(
    val id: String,
    override val name: String,

    ): DropDownCommonInterface