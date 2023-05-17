@file:Suppress("DEPRECATION")
package com.vxplore.thetimesgroup.screens
import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.ui.theme.GreyLight
import com.vxplore.thetimesgroup.viewModels.BaseViewModel
import com.vxplore.thetimesgroup.viewModels.VendorAddSuccessViewModel


@Composable
fun VendorAddSuccessfulScreen(viewModel: VendorAddSuccessViewModel = hiltViewModel(),baseViewModel: BaseViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        val activity = LocalContext.current as Activity
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(GreyLight)
            .padding(5.dp)) {

            val activity = LocalContext.current as Activity
            Image(painter = painterResource(id = R.drawable.ic_baseline_keyboard_backspace_24),
                contentDescription = "back button",
                modifier = Modifier
                    .clickable {
                        activity.onBackPressed()
                    }
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Thank you",
            color = Color.DarkGray,
            modifier = Modifier
                .padding(start = 15.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Vendor  ${baseViewModel.addedVendorName.value}  added.",
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 15.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(30.dp))
        val context = LocalContext.current



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
        ) {


            Button(
                onClick = {
                    viewModel.onAddVendorSuccessToAddVendor()
                   // Toast.makeText(context, "To AddVendor", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = 15.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = GreenLight)
            ) {
                Text(
                    text = "Add Vendor",
                    color = Color.White,
                   fontSize = 17.sp,
                    maxLines = 1
                )
            }

            Button(
                onClick = {
                    viewModel.onAddVendorSuccessToDashboard()
                   // activity.recreate()

                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = 15.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
            ) {
                Text(
                    text = "Dashboard",
                    color = Color.White,
                    fontSize = 17.sp,
                    maxLines = 1
                )
            }


        }

    }

}


//}