package com.vxplore.thetimesgroup.custom_views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.vxplore.core.domain.model.PaperCode
import com.vxplore.thetimesgroup.screens.PaperSold
import com.vxplore.thetimesgroup.screens.getPaperSoldDetails
import com.vxplore.thetimesgroup.ui.theme.MyColors
import com.vxplore.thetimesgroup.viewModels.DashboardViewModel

@Composable
fun MyDoughnutChart(
    paperSoldList: List<PaperCode>,
    viewModel: DashboardViewModel,
    size: Dp = 130.dp,
    thickness: Dp = 45.dp
) {
    //val values: List<Float> = listOf(10f,15f,20f,15f,10f,10f,10f,10f)
//    val values: List<Float> = getPaperSoldDetails().map { it.floatValue }.toList()
    //val values: List<Float> = paperSoldList.map {it.floatValue}
    val values: List<Float> = paperSoldList.map { it.paper_percent_float.toFloat() }
    val sumOfValues = values.sum()
    val proportions = values.map { it * 100 / sumOfValues }
    val sweepAngles = proportions.map { 360 * it / 100 }
Column() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(25.dp)
    ) {
        Box(modifier = Modifier.weight(1f, true)) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                for (i in values.indices) {
                    val legendColor = paperSoldList[i].colorCode
                    //DisplayLegend(color = paperSoldList[i].color, legend = paperSoldList[i].name+" - ${paperSoldList[i].price}"+"(${paperSoldList[i].percentage})")
                    //  DisplayLegend(color = Color(paperSoldList[i].colorCode.toInt()), legend = paperSoldList[i].name+" - ${paperSoldList[i].paper_count}"+"(${paperSoldList[i].?paper_percent}%)")
                    DisplayLegend(
                        color =Color(android.graphics.Color.parseColor(paperSoldList[i].colorCode)),
                        legend = paperSoldList[i].name + " - ${paperSoldList[i].paper_count}" + "(${paperSoldList[i].paper_percent})"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(32.dp))

        Box(
            modifier = Modifier
                // .weight(1f, true)
                .padding(end = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(size = size)
                    .align(Alignment.TopEnd)
            ) {
                var startAngle = -90f
                for (i in values.indices) {
                    drawArc(
                        color = Color(android.graphics.Color.parseColor(paperSoldList[i].colorCode)),
                        startAngle = startAngle,
                        sweepAngle = sweepAngles[i],
                        useCenter = false,
                        style = Stroke(width = thickness.toPx(), cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngles[i]
                }
            }
            Column(modifier = Modifier.wrapContentSize()) {
                Text(
                    text = viewModel.totalPapersSold.value,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = "Total",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray,
                )
            }
        }//box
    }//parent Row

    Text(
        text = "Total Paper Circulations",
        modifier = Modifier
            .padding(0.dp, 0.dp, 28.dp, 0.dp)
            .align(Alignment.End),
        color = Color.Gray,
        textDecoration = TextDecoration.Underline,
        fontSize = 12.sp
    )
}


}//MyDoughnutChart

@Composable
fun DisplayLegend(color: Color, legend: String) {

    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .background(color = color, shape = RectangleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = legend, color = Color.Black, fontSize = 13.sp)
    }
}