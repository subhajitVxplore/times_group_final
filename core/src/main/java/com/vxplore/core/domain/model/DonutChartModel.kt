package com.vxplore.core.domain.model


data class DonutChartModel(
    val message: String,
    val papers: List<PaperCode>,
    val status: Boolean,
    val total_sold_papers: Int
)

data class PaperCode(
    val colorCode: String,
    val name: String,
    val paper_count: Int,
    val paper_percent: String,
    val paper_percent_float: String

)
//data class DonutChartModel(
//    val message: String,
//    val papers: List<PaperCode>,
//    val status: Boolean,
//    val total_sold_papers: String
//)
//
//data class PaperCode(
//    val colorCode: String,
//    val name: String,
//    val paper_count: Int,
//    val paper_percent: String,
//    val paper_percent_float: String
//
//)