@file:Suppress("DEPRECATION")

package com.vxplore.thetimesgroup.screens

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vxplore.core.common.AppRoutes
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.extensions.screenHeight
import com.vxplore.thetimesgroup.extensions.screenWidth
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.viewModels.MobileNoScreenViewModel
import io.ktor.util.reflect.*
import java.util.regex.Pattern

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MobileNoScreen(
    viewModel: MobileNoScreenViewModel = hiltViewModel(),navController :NavController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val mContext = LocalContext.current
    //val navController :NavController

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
    ) {
        val activity = LocalContext.current as Activity
        Image(painter = painterResource(id = R.drawable.ic_baseline_keyboard_backspace_24),
            contentDescription = "countryImage",
            modifier = Modifier.clickable {
                // activity.onBackPressed()
                activity.finishAffinity()
            }
        )

        Text(
            text = "Register or SignIn with your Mobile number",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.h5,
        )

        Surface(
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),

            //  .padding(5.dp)
//            modifier = Modifier.fillMaxWidth().height(50.dp)

        ) {
            Row(
                // modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(5.dp)
                modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.indian_flag),
                    contentDescription = "Flag",
                    modifier = Modifier
                        .width(40.dp)
                        .wrapContentHeight()
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = "+91",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .align(Alignment.CenterVertically)
                        .padding(5.dp, 0.dp, 0.dp, 0.dp)
                )

                val maxLength = 9
              //  val pattern = remember { Regex("^\\d+\$") }

                //var digitsOnly= listOf(0,1,2,3,4,5,6,7,8,9)
                val digitsOnly: Boolean = TextUtils.isDigitsOnly(viewModel.MobileNoText.value)
                TextField(
                    value = viewModel.MobileNoText.value ?: "",
                    singleLine = true,
                    onValueChange = {
                        val digitsOnly: Boolean = TextUtils.isDigitsOnly(viewModel.MobileNoText.value)

                        if ((it.length <= 10)) {
                            viewModel.MobileNoText.value = it
                        } else if (it.length > maxLength) {
                            keyboardController?.hide()
                        } else {
                            Toast.makeText(mContext, "Can not be more than 10", Toast.LENGTH_SHORT)
                                .show()
                            //keyboardController?.hide()
                        }

                    },
                    textStyle = TextStyle.Default.copy(fontSize = 23.sp),
                    placeholder = {
                        Text(
                            text = "your mobile number",
                            style = MaterialTheme.typography.h6,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .background(Color.White),

                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        //keyboardType = KeyboardType.instanceOf(DigitsKeyListener.getInstance("0,1,2,3,4,5,6,7,8,9"))
                        keyboardType = KeyboardType.Number
                    ),

                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,//hide the indicator
                        unfocusedIndicatorColor = Color.White
                    )

                )
            }

        }



        Row(
            // modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(5.dp)
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp, 7.dp, 0.dp, 0.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_round_check_circle_24),
                contentDescription = "check",
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = "you will receive important updates and informations from us over the whatsapp",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.Gray
            )

        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomCenter)
            ) {
                val context = LocalContext.current

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            //Toast.makeText(context, "continue", Toast.LENGTH_SHORT).show()
                            //onContinueClick()
                            if (viewModel.MobileNoText.value != null && viewModel.MobileNoText.value!!.length < 10) {
                                Toast.makeText(context, "10 digits required", Toast.LENGTH_SHORT).show()
                            } else {
                                //navController.navigate(AppRoutes.OTP+ "/${viewModel.MobileNoText.value}",)
                                //viewModel.onMobToOtp("${viewModel.MobileNoText.value}")
                                viewModel.loadingg.value=true
                                viewModel.sendOtp("${viewModel.MobileNoText.value}")

                            }
                            //  navController.navigate(Routes.Settings.route + "/$counter")
                        },
                        enabled = viewModel.loadingButton.value,
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        if (!viewModel.loadingg.value){
                            Text(
                                text = "Continue",
                                color = Color.White,
                                style = MaterialTheme.typography.h6,
                            )
                        }else{
                            CircularProgressIndicator(color = Color.White)
                        }

                    }


                  //  if (viewModel.loadingButton.value){
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .size(width = screenWidth * 0.15f, height = screenHeight * 0.15f)
//                                .padding(40.dp),
//                            color = GreenLight,
//                            strokeWidth = 5.dp,
//                        )
                   // }

                }


                Text(
                    text = "We respect your privacy. We'll only share informations that's important for you - no spam!",
                    // style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .padding(7.dp, 7.dp, 0.dp, 0.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            }
        }
    }

    LaunchedEffect(viewModel.notifier.value) {
        if (viewModel.notifier.value.isNotEmpty()) {
            Toast.makeText(mContext, viewModel.notifier.value, Toast.LENGTH_SHORT).show()
            viewModel.notifier.value = ""
        }
    }

    LaunchedEffect(true){
        viewModel.loadingButton.value=true
        viewModel.loadingg.value=false
    }
}
////////////////////////////////////////////////////////////////////////////


val MOBILE_NO_PATTERN: Pattern = Pattern.compile("[0-9]{10}")

//    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
//            "\\@" +
//            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
//            "(" +
//            "\\." +
//            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
//            ")+"
//)

fun checkMobile(mobile_no: String): Boolean {
    return MOBILE_NO_PATTERN.matcher(mobile_no).matches()
}