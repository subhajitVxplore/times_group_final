package com.vxplore.thetimesgroup.screens.dashboard_content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vxplore.core.domain.model.Vendor
import com.vxplore.thetimesgroup.custom_views.MyDoughnutChart
import com.vxplore.thetimesgroup.extensions.bottomToUp
import com.vxplore.thetimesgroup.extensions.screenHeight
import com.vxplore.thetimesgroup.extensions.screenWidth
import com.vxplore.thetimesgroup.extensions.upToBottom
import com.vxplore.thetimesgroup.screens.getPaperSoldDetails
import com.vxplore.thetimesgroup.ui.theme.*
import com.vxplore.thetimesgroup.viewModels.DashboardViewModel

@Composable
fun DashboardMainScreen(openDrawer: () -> Unit, viewModel: DashboardViewModel) {
    val ctx = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {viewModel.onDashboardToBilling()},
                modifier = Modifier.size(55.dp),
                backgroundColor = WhiteGray,
                contentColor = BrownGray) {
                Icon(Icons.Rounded.Add, contentDescription = "Add",modifier = Modifier.size(40.dp))
            }
        }
    ) { padding->
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.weight(1f)) {
            Column(content = { Box(modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()) {
                        Row( modifier = Modifier
                            .padding(13.dp)
                            .wrapContentSize()
                            .align(Alignment.Center)) {
                            Column(modifier = Modifier
                                .wrapContentHeight()
                                .weight(1.0f, true)
                                .background(GreenLight, shape = RoundedCornerShape(5.dp))) {
                                Text(text = "Today's Total",color = Color.White,fontSize = 12.sp,modifier = Modifier.padding(10.dp, 5.dp, 0.dp, 0.dp))

                                Text(
                                    text = viewModel.todayPaperSold.value,
                                    style = MaterialTheme.typography.h5,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Text(
                                    text = viewModel.todayEachPaperSold.value,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 5.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .background(
                                            Color.Black,
                                            shape = RoundedCornerShape(0.dp, 0.dp, 5.dp, 5.dp)
                                        ),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    Text(
                                        text = "This Month ${viewModel.thisMonthPaperSold.value}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(width = 10.dp))


                            Column(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(1.0f, true)
                                    .background(PinkLight, shape = RoundedCornerShape(5.dp))
                            ) {
                                Text(
                                    text = "Today's Return",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(10.dp, 5.dp, 0.dp, 0.dp)
                                )
                                Text(
                                    text = viewModel.todayPaperReturn.value,
                                    style = MaterialTheme.typography.h5,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Text(
                                    text = viewModel.todayEachPaperReturn.value,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 5.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .background(
                                            Color.Black,
                                            shape = RoundedCornerShape(0.dp, 0.dp, 5.dp, 5.dp)
                                        ),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    Text(
                                        text = "This Month ${viewModel.thisMonthPaperReturn.value}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp, vertical = 5.dp
                                        )
                                    )
                                }
                            }
                        }

                    }
                   // MyDoughnutChart(getPaperSoldDetails())

                if (viewModel.totalPapersSold.value != ""){

                    MyDoughnutChart(viewModel.paperCodes.collectAsState().value,viewModel)

                    Spacer(modifier = Modifier.height(20.dp))

                   showVendorsList(vendorList = viewModel.vendors.collectAsState().value,viewModel.vendorsLoading.value)
                //CircularProgressIndicator()
                }

                })
        }
    }
    }

    LaunchedEffect(true){
        viewModel.getTodayPaperSoldByUserId()
        viewModel.getVendors()
        viewModel.calculateDonutSweepAngles()
        viewModel.getDonutChartData()
        viewModel.getDistributorDetails()
    }

}//dashBoardMain

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun showVendorsList(vendorList: List<Vendor>,loading: Boolean,) {
    Surface(
        border = BorderStroke(0.7.dp, Color.LightGray),
        color = GreyLight,
        shape = RoundedCornerShape(3.dp),
        //elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(5.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp, 10.dp, 0.dp, 10.dp)
        ) {

            Text(
                text = "Top Vendors",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f, true)
            )
            Text(
                text = "Daily Avg.",
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(15.dp, 0.dp, 0.dp, 0.dp)
            )
            Text(
                text = "Return Avg.",
                fontSize = 12.sp,
                modifier = Modifier.weight(1f, true)
            )
            Text(
                text = "Payment Due",
                fontSize = 12.sp,
                modifier = Modifier.weight(1f, true)
            )
        }
    }

///----------------------------------------------------------------///

    AnimatedContent(
        targetState = loading,
        transitionSpec = {
            if(targetState && !initialState) {
                upToBottom()
            } else {
                bottomToUp()
            }
        }
    ) {
        if (!it) {

            LazyColumn() {
                itemsIndexed(items = vendorList) { index, vendorr ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp, 10.dp, 0.dp, 10.dp)) {

                        Text(
                            text = vendorr.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f, true)
                                .padding(5.dp, 0.dp, 0.dp, 0.dp))

                        Text(
                            text = vendorr.daily_avg,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f, true)
                                .padding(25.dp, 0.dp, 0.dp, 0.dp)
                        )
                        Text(
                            text = vendorr.return_avg,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f, true)
                                .padding(20.dp, 0.dp, 0.dp, 0.dp)
                        )
                        Text(
                            text = vendorr.payment_due,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f, true)
                                .padding(20.dp, 0.dp, 0.dp, 0.dp)
                        )

                    }
                    Divider(color = GreyLight, thickness = 0.8.dp, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp))
                }
            }



        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(width = screenWidth * 0.15f, height = screenHeight * 0.15f)
                        .padding(bottom = screenHeight * 0.05f),
                    color = GreenLight,
                    strokeWidth = 5.dp,
                )
            }

        }
    }

 ///////////////////////////////////////////////////////////////////


}


