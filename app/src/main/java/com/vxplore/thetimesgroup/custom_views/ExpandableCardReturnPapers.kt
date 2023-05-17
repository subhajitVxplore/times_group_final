@file:Suppress("DEPRECATION")

package com.vxplore.thetimesgroup.custom_views

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vxplore.core.domain.model.Paper
import com.vxplore.thetimesgroup.extensions.bottomToUp
import com.vxplore.thetimesgroup.extensions.upToBottom
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.ui.theme.GreyLight
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun ExpandableCardReturnPapers(
    loading: Boolean,
    header: String, // Header
    paperList: List<Paper>,
    viewModel: BillingScreenViewModel,
    onReturnPriceChange: (String, Int, Float) -> Unit
) {
    val rotationState by animateFloatAsState(if (viewModel.expandReturn.value) 180f else 0f) // Rotation State
    val price: String = ""
            Card(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 15.dp),
                elevation = 10.dp,
                backgroundColor = Color.White,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(viewModel.stroke.value.dp, Color.LightGray),
                onClick = {
                    viewModel.expandReturn.value = !viewModel.expandReturn.value
                    viewModel.expandCoupon.value=false
                    //viewModel.stroke.value = if (viewModel.expandReturn.value) 2 else 1
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GreyLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().height(42.dp)
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween // Control the header Alignment over here.
                    ) {
                        Text(
                            text = header,
                            color = Color.DarkGray, // Header Color
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )


                        if (paperList.isNotEmpty()) {
                            val returnValues =
                                viewModel.returnsTotal.value
                            Text(
                                text = if (returnValues != 0f) "₹-${returnValues}"
                                else "₹-0",
                                color = Color.DarkGray, // Header Color
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                                    //.weight(.9f)
                                    .padding(end = 7.dp)
                            )
                        }
//                        IconButton(
//                            modifier = Modifier
//                                .rotate(rotationState)
//                                .weight(.1f),
//                            onClick = {
//                              //  viewModel.expandReturn.value = true
//                                viewModel.expandReturn.value = !viewModel.expandReturn.value
//                               // viewModel.expandCoupon.value = !viewModel.expandCoupon.value
////                                viewModel.stroke.value = if (viewModel.expandReturn.value) 2 else 1
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.KeyboardArrowDown,
//                                tint = Color.Gray, // Icon Color
//                                contentDescription = "Drop Down Arrow",
//                            )
//                        }
                    }//Row
                    Divider(color = Color.LightGray, thickness = 0.8.dp)

                    AnimatedContent(
                        targetState = loading,
                        transitionSpec = {
                            if (targetState && !initialState) {
                                upToBottom()
                            } else {
                                bottomToUp()
                            }
                        }
                    ) {
                        if (!it) {
                            if (viewModel.expandReturn.value) {
                                Spacer(modifier = Modifier.height(7.dp))

                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                Column(
                                    modifier = Modifier
                                        //.height(120.dp)
                                        .fillMaxWidth()
                                        //.verticalScroll(rememberScrollState())
                                ) {
                                    //itemsIndexed(items = paperList) { index, paperr ->
                                    for ((index, paperr) in paperList.withIndex()) {
                                        Row(Modifier.wrapContentHeight()) {
                                            Column(
                                                modifier = Modifier
                                                    .wrapContentSize()
                                                    .align(Alignment.CenterVertically)
                                                    .padding(13.dp, 0.dp, 0.dp, 0.dp)
                                            ) {
                                                Text(text = paperr.value, color = Color.DarkGray)
                                                Text(
                                                    text = "Yesterday Total Paper ${paperr.previous_paper_count}, @ ₹${paperr.previous_price}",
                                                    color = Color.Gray,
                                                    fontSize = 10.sp
                                                )
                                                // Text(text = "Yesterday Total Paper ${paperr.previous_paper_count} (Subscription 300)", color = Color.Gray, fontSize = 10.sp)
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .wrapContentHeight()
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.TopEnd
                                            ) {
                                                Surface(
                                                    modifier = Modifier
                                                        .wrapContentSize()
                                                        .padding(horizontal = 15.dp),
                                                    color = Color.White
                                                ) {

                                                    var value by remember(price) {
                                                        mutableStateOf(price)
                                                    }
                                                    BasicTextField(
                                                        value = value,
                                                        onValueChange = {
                                                            if (paperr.previous_paper_count > 0){
                                                                value = it
                                                                onReturnPriceChange(value,index,paperr.previous_price)
                                                                viewModel.returnPapersKey.forEach{
                                                                    it.key=paperr.key
                                                                }
                                                            }
                                                        },
                                                        keyboardOptions = KeyboardOptions(
                                                            imeAction = ImeAction.Done,
                                                            keyboardType = KeyboardType.Number
                                                        ),
                                                        maxLines = 1,
                                                        modifier = Modifier
                                                            .width(80.dp)
                                                            .height(37.dp),
                                                        textStyle = TextStyle.Default.copy(fontSize = 17.sp)
                                                    ) {
                                                        TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                                            value = value,
                                                            innerTextField = it,
                                                            enabled = true,
                                                            singleLine = true,
                                                            visualTransformation = VisualTransformation.None,
                                                            interactionSource = MutableInteractionSource(),
                                                            contentPadding = PaddingValues(all = 4.dp),
                                                            colors = textFieldColors(backgroundColor = Color.White)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(7.dp))
                                    }


                                }//lazy column
                                Spacer(modifier = Modifier.height(10.dp))
//                                    Divider(color = Color.LightGray, thickness = 0.8.dp, modifier = Modifier.padding(bottom = 5.dp))
//                                    Row(
//                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.SpaceBetween // Control the header Alignment over here.
//                                    ) {
//
//                                        if (paperList.isNotEmpty()) {
//                                            val returnValues =
//                                                viewModel.returnsTotal.value
//                                            Text(
//                                                text = if (returnValues != 0) "Total Return Price = ₹${returnValues}"
//                                                    else "Total Return Price = ₹0",
//                                                color = Color.DarkGray, // Header Color
//                                                fontSize = 15.sp,
//                                                textAlign = TextAlign.Start,
//                                                fontWeight = FontWeight.Normal,
//                                                modifier = Modifier
//                                                    .weight(.9f)
//                                                    .padding(start = 8.dp)
//                                            )
//                                        }
//                                    }
//                                    Spacer(modifier = Modifier.height(10.dp))

                            }//column
                    }
                                    } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
//                      .size(width = screenWidth * 0.15f, height = screenHeight * 0.15f)
                        .padding(40.dp),
                    color = GreenLight,
                    strokeWidth = 5.dp,
                )
            }

        }
    }





                }
            }



}