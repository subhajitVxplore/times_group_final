package com.vxplore.thetimesgroup.custom_views


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun showPapersTakenList(
    loading: Boolean,
    paperList: List<Paper>, viewModel: BillingScreenViewModel,
    onPriceChange: (String, Int, Float) -> Unit
) {
//    LazyColumn(modifier = Modifier.height(300.dp).fillMaxWidth()) {
//                itemsIndexed(items = paperList) { index, paperr ->

    var price: String = ""


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

            Column(modifier = Modifier.fillMaxSize()) {

                for ((index, paperr) in paperList.withIndex()) {
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Row(Modifier.wrapContentHeight()) {

                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                Text(text = paperr.value, color = Color.Gray)
                                Text(
                                    text = "₹${paperr.todays_price}",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                        .align(Alignment.End)
                                )
                            }


                            var value by remember(price) {
                                mutableStateOf(price)
                            }


                            BasicTextField(
                                value = value,
                                onValueChange = {
                                    if (paperr.todays_price > 0f){
                                        value = it
                                        onPriceChange(value, index, paperr.todays_price)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number
                                ),
                                maxLines = 1,
                                modifier = Modifier
                                    .width(125.dp)
                                    .height(40.dp)
                                    .padding(horizontal = 15.dp),
                                textStyle = TextStyle.Default.copy(fontSize = 20.sp)
                            ) {
                                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                    value = value,
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
                    Spacer(modifier = Modifier.height(7.dp))
                }//for loop

                if (paperList.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp),
                        backgroundColor = Color.White,
                        border = BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier.padding(horizontal = 15.dp)
                            .fillMaxWidth().wrapContentSize(),
                        elevation = 15.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween // Control the header Alignment over here.
                        ) {

                            if (paperList.isNotEmpty()) {
                                val takenValues =
                                    viewModel.takenPapersTotal.value
                                Text(
                                    text = if (takenValues != 0f) "Total TakenPaper Price = ₹${takenValues}"
                                    else "Total TakenPaper Price = ₹0",
                                    color = Color.DarkGray, // Header Color
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        //.weight(.9f)
                                        .padding(start = 8.dp)
                                )
                            }
                        }
                    }

                }

            }




        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
//                        .size(width = screenWidth * 0.15f, height = screenHeight * 0.15f)
                        .padding(40.dp),
                    color = GreenLight,
                    strokeWidth = 5.dp,
                )
            }
        }
    }




}



