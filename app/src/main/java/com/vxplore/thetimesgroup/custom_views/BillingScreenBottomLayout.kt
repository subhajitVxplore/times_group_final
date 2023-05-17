package com.vxplore.thetimesgroup.custom_views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vxplore.thetimesgroup.extensions.bottomToUp
import com.vxplore.thetimesgroup.extensions.upToBottom
import com.vxplore.thetimesgroup.mainController.MainActivity
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.ui.theme.PinkLight
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun BillingScreenBottomLayout(viewModel: BillingScreenViewModel) {

//    AnimatedContent(
//        targetState = loading,
//        transitionSpec = {
//            if (targetState && !initialState) {
//                upToBottom()
//            } else {
//                bottomToUp()
//            }
//        }
//    ) {
//        if (!it) {


    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomEnd)
            ) {

//----------------------------Due Payment Layout----------------------------------------------------
                Spacer(modifier = Modifier.height(5.dp))
                // val context= LocalContext.current
                Divider(
                    color = Color.LightGray,
                    thickness = 0.8.dp,
                    // modifier = Modifier.padding(horizontal = 5.dp)
                )
                Surface(
                    //  shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 15.dp),
                    //  color = PinkLight,
                    contentColor = Color.White
                ) {
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, true)
                                .align(Alignment.CenterVertically)
                                .padding(start = 5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .wrapContentSize()
                                //.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "Due Payment",
                                    color = Color.DarkGray,
                                    modifier = Modifier.align(
                                        Alignment.CenterVertically
                                    )
                                )
                                Text(
                                    text = "   ( With previous ₹ ${viewModel.previousDue.value} Due )",
                                    color = Color.Gray,
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }

                        Text(
                            text = "₹${viewModel.currentDue.value}/-",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 10.dp)
                                .align(Alignment.CenterVertically),
                            fontSize = 20.sp
                        )
                    }
                }//surface\
                Divider(
                    color = Color.LightGray,
                    thickness = 0.8.dp,
                    // modifier = Modifier.padding(horizontal = 5.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                //----------------Mode of Payment Cash Layout-------------------//
                Row(Modifier.wrapContentHeight()) {
                    Text(
                        text = "Mode of Payment",
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(), contentAlignment = Alignment.TopEnd
                    ) {
                        Row(modifier = Modifier.wrapContentSize()) {
                            Text(
                                text = "Cash ₹",
                                color = Color.DarkGray,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            BasicTextField(
                                value = viewModel.cashPaymentText.value,
                                onValueChange = {
                                    try {
                                        viewModel.cashPaymentText.value=it
                                        viewModel.cashPayment.value = viewModel.cashPaymentText.value.toInt()
                                        //  viewModel.calculateCurrentDue()
                                    } catch (e: Exception) {
                                        println(e)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number
                                ),
                                maxLines = 1,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(37.dp)
                                    .padding(start = 5.dp, end = 15.dp),
                                // textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                                textStyle = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),

                                ) {
                                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                    value = viewModel.cashPayment.value.toString(),
                                    innerTextField = it,
                                    enabled = true,
                                    singleLine = true,
                                    visualTransformation = VisualTransformation.None,
                                    interactionSource = MutableInteractionSource(),
                                    contentPadding = PaddingValues(all = 4.dp),
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                                        backgroundColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))

                //----------------Generate Bill Button Layout-------------------//
                val context = LocalContext.current as MainActivity
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 15.dp)
                ) {
                    Row(modifier = Modifier.weight(1f).padding(start = 15.dp)) {
                      //  context.ShowItemFileLayout(viewModel, context = context)

                        val showDialog = remember { mutableStateOf(false) }

                        if (showDialog.value)
                            CustomDialog(value = "", setShowDialog = {
                                showDialog.value = it
                            },
                                onClickPrintBill = {
                                    viewModel.checkIfDeviceFound()
                                },
                                onClickViewBill = {

                                    viewModel.generateBillByJson()
                                    viewModel.searchVendorQuery=""
                                    viewModel.clearVendorsQuery()
                                }
                                ,viewModel)


                        Button(
                            onClick = {
                                showDialog.value = true
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = GreenLight),
                            shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp),
                            //enabled = viewModel.loadingBill.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)

                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = viewModel.generateBillButtonText,
                                    fontSize = 17.sp,
                                    color = Color.White,
                                )
                            }
                        }//Button
                    }

                    Card(
                        shape = RoundedCornerShape(topEnd = 10f, bottomEnd = 10f),
                        backgroundColor = Color.DarkGray,
                        modifier = Modifier
                            .height(50.dp)
                            .width(100.dp)
                    ) {

                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Text(
                                    text = "₹${viewModel.balanceAmount.value}/-",
                                    color = Color.White, // Header Color
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        //.align(Alignment.TopEnd)
                                        //.padding(start = 10.dp)
                                        //.weight(.9f)
                                        .padding(end = 10.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }//Column
        }
    }




//        } else {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator(
//                    modifier = Modifier
////                        .size(width = screenWidth * 0.15f, height = screenHeight * 0.15f)
//                        .padding(40.dp),
//                    color = GreenLight,
//                    strokeWidth = 5.dp,
//                )
//            }
//        }
//    }

}

