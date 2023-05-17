@file:Suppress("DEPRECATION")

package com.vxplore.thetimesgroup.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.custom_views.OtpTextField
import com.vxplore.thetimesgroup.ui.theme.GreenDark
import com.vxplore.thetimesgroup.viewModels.OtpViewModel

@Composable
fun OtpScreen( viewModel: OtpViewModel  = hiltViewModel()) {

    val mContext = LocalContext.current

    var otpValue by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),

        // verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val activity = LocalContext.current as Activity
        Image(painter = painterResource(id = R.drawable.ic_baseline_keyboard_backspace_24),
            contentDescription = "countryImage",
            modifier = Modifier.clickable { activity.onBackPressed() }
            )

        Text(
            text = "Verify OTP",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.h5,
        )
        Text(
            text = "you have sent OTP on your mobile number",
            // style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(10.dp,7.dp,0.dp,0.dp),
            color = Color.Gray
        )


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.padding(25.dp,0.dp,0.dp,0.dp)
            ) {

                Text(
                    text = "+91",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterVertically),

                    )

                Text(
                    text = "${viewModel.number}",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterVertically)
                )
                Image(painter = painterResource(id = R.drawable.ic_edit_icon),
                    contentDescription = "Edit",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(0.dp, 10.dp, 10.dp, 10.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {

                            // navController.navigateUp()
                            //navController.navigate(AppRoutes.MOBILE_NO +"$mobileNoText")
                            //navController.navigate(AppRoutes.MOBILE_NO)
                            //viewModel.onOtpToMob("${viewModel.number}")
                            viewModel.onOtpToMob()
                        },

                )

            }

        }


///////////////////////////////////////////////////////////

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White),
                //.padding(20.dp),
            color = Color.White
        ) {

            OtpTextField(
                otpText = otpValue,
                onOtpTextChange = { value, otpInputFilled ->
                    otpValue = value
                }
            )
        }

/////////////////////////////////////////////////////////
        Row(
            // modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(5.dp)
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(25.dp, 0.dp, 0.dp, 0.dp)

        ) {

            Text(
                text = "Haven't received?",
                // style = MaterialTheme.typography.h3,
                modifier = Modifier.align(Alignment.CenterVertically),
            )

            val cont= LocalContext.current
            TextButton(onClick = {
                Toast.makeText(cont, "", Toast.LENGTH_SHORT).show()
            }) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Resend it",
                    textDecoration = TextDecoration.Underline,
                    color = GreenDark
                )
            }

        }
//////////////////////////////////////////////////////////

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .align(Alignment.BottomCenter)) {
                val context= LocalContext.current
                Button(
                    onClick = {
//                        navController.navigate(AppRoutes.REGISTER)
                             // viewModel.onOtpToRegister("${viewModel.number}")
                      //  Toast.makeText(context, "$otpValue", Toast.LENGTH_SHORT).show()
                        if (otpValue.length<4){
                            Toast.makeText(mContext, "Enter 4 digit OTP", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.loadingg.value=true
                            viewModel.verifyOtp("$otpValue")
                        }

                    }, shape = RoundedCornerShape(5.dp), modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    if (!viewModel.loadingg.value){
                    Text(text = "Verify OTP", color = Color.White,style = MaterialTheme.typography.h6,)
                    }else{
                        CircularProgressIndicator(color = Color.White)
                    }
                }

            }

        }



    }
    
    
    
    
}
