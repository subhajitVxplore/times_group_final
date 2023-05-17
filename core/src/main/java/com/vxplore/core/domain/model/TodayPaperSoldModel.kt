package com.vxplore.core.domain.model

data class TodayPaperSoldModel(
    val message: String,
    val paperReturn: PaperReturn,
    val paperSold: PaperSold,
    val status: Boolean
)

data class PaperReturn(
    val each_paper_return: String,
    val this_month: Int,
    val todays_total: Int
)

data class PaperSold(
    val each_paper_sold: String,
    val this_month: Int,
    val todays_total: Int
)