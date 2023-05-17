package com.vxplore.thetimesgroup.custom_views

import android.annotation.SuppressLint
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.model.Coupon
import com.vxplore.thetimesgroup.extensions.bottomToUp
import com.vxplore.thetimesgroup.extensions.upToBottom
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun HorizontalScrollableCoupon(
    couponList: List<Coupon>,
    viewModel: BillingScreenViewModel,
    price: String = "",
    onPriceChange: (String, Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                   // .padding(vertical = 10.dp)
                    .height(40.dp)
                    .horizontalScroll(state = scrollState),
                content = {
                    for ((index, coupons) in couponList.withIndex()) {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            backgroundColor = Color.Gray,
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentWidth()
                                .padding(horizontal = 5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .wrapContentWidth()
                            ) {

                                //if (couponList.isNotEmpty()){
//                                if (couponList != null) {
                                if (couponList.isEmpty() or (couponList == null)) {
                                    ShimmerAnimation()

                                } else {

                                    val context= LocalContext.current
                                    Text(
                                        "₹" + coupons.value.toString(),
                                        modifier = Modifier
                                            .wrapContentHeight()
                                            .width(50.dp)
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 10.dp)
                                            .clickable{
                                                Toast.makeText(context,coupons.key,Toast.LENGTH_SHORT).show()
                                            } ,
                                        style = TextStyle(color = Color.Black, fontSize = 15.sp)
                                    )
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(50.dp),
                                        shape = RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp),
                                        color = Color.White
                                    ) {

                                        MyBasicTextField(text = price,
                                            onValueChange = {
                                            onPriceChange(it, index, coupons.value)
                                                //viewModel.calculateCoupon()
                                               // viewModel.calculateCurrentDue()
                                        })
                                    }
                                }
//                                // ShimmerAnimation()
                            }

                        }
                    }
                })
    }

//////////////////////////////////////////////////////////////////////////////////////////
    @Composable
    fun ShimmerItem(
        brush: Brush
    ) {

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .background(brush)
                .width(100.dp)
        ) {
            Text(
                "loading...",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp),
                style = TextStyle(color = Color.Gray, fontSize = 15.sp)
            )
        }
    }


    @Composable
    fun ShimmerAnimation(
    ) {

        val ShimmerColorShades = listOf(
            Color.LightGray.copy(0.9f),
            Color.LightGray.copy(0.2f),
            Color.LightGray.copy(0.9f)
        )
        val transition = rememberInfiniteTransition()
        val translateAnim by transition.animateFloat(

            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )

        val brush = Brush.linearGradient(
            colors = ShimmerColorShades,
            start = Offset(10f, 10f),
            end = Offset(translateAnim, translateAnim)
        )

        ShimmerItem(brush = brush)
    }

