package com.vxplore.core.domain.model

import com.vxplore.core.common.DropDownCommonInterface

data class AllStatesModel(
    val message: String,
    val states: List<State>,
    val status: Boolean
)

data class State(
    val id: String,
    override val name: String
): DropDownCommonInterface

//data class Paper(override var name: String, val price: Int) : DropDownItem