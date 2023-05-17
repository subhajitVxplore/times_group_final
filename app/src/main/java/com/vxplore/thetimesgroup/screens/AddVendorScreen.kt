@file:Suppress("DEPRECATION")

package com.vxplore.thetimesgroup.screens

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.custom_views.*
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.ui.theme.GreyLight
import com.vxplore.thetimesgroup.viewModels.AddVendorViewModel
import com.vxplore.thetimesgroup.viewModels.BaseViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AddVendorScreen(viewModel: AddVendorViewModel = hiltViewModel(),baseViewModel: BaseViewModel) {

    val suggestions = viewModel.pincodes.collectAsState().value
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(GreyLight)
                .padding(5.dp)
        ) {

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
            Text(
                text = "Add Vendor",
                color = Color.DarkGray,
                fontSize = 17.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }



        OutlinedTextField(
            value = viewModel.vendorNameText.value,
            onValueChange = { viewModel.vendorNameText.value = it },
            label = { Text("Vendor Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp),
            singleLine = true
        )


        OutlinedTextField(
            value = viewModel.vendorMobileText.value,
            onValueChange = {
                if (it.length <= 10) viewModel.vendorMobileText.value = it
                            },
            label = { Text("Vendor Phone") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
        )

        OutlinedTextField(
            value = viewModel.vendorEmailAddressText.value,
            onValueChange = { viewModel.vendorEmailAddressText.value = it },
            label = { Text("Vendor Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Email
            ),
        )

        Spacer(modifier = Modifier.height(10.dp))
        PincodeSearchField(viewModel)
        //SearchPincodesSection(viewModel)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.TopCenter
        ) {


            Text(
                text = "You can add multiple (,) separated pinCode(s).",
                color = Color.Gray,
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 14.sp
            )

            ///suggestion dropdown section calling
            AnimatedContent(targetState = viewModel.visible) { state ->
                when (state) {
                    true -> PincodesSuggestionsSection(viewModel)
                    false -> Surface() { state }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        val context = LocalContext.current

        fun String.isValidEmail(): Boolean {
            return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
                .matches()
        }

        val keyboardController = LocalSoftwareKeyboardController.current

        Button(
            onClick = {
                /// viewModel.onAddVendorToAddVendorSuccess()
                if (viewModel.vendorNameText.value.isEmpty()) {
                    Toast.makeText(context, "vendorName can not be empty !!", Toast.LENGTH_SHORT)
                        .show()
                } else if (viewModel.vendorMobileText.value.isEmpty()) {
                    Toast.makeText(context, "vendorMobile can not be empty !!", Toast.LENGTH_SHORT)
                        .show()
//                    if ((viewModel.vendorMobileText.value.length > 9)) {
//                        Toast.makeText(mContext, "Can not be more than 10", Toast.LENGTH_SHORT)
//                            .show()
//                    }
                      //  keyboardController?.hide()

                } else if (!viewModel.vendorEmailAddressText.value.isValidEmail()) {
                    Toast.makeText(context, "Please enter valid email !!", Toast.LENGTH_SHORT)
                        .show()
                } else if (viewModel.currentPincode.value.isEmpty()) {
                    Toast.makeText(context, "areaPincodes can not be empty !!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addVendor()
                    baseViewModel.addedVendorName.value=viewModel.vendorNameText.value
                }
                // Toast.makeText(context, "Generate Bill${viewModel.pincodes.value}", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(53.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = GreenLight)
        ) {
            Text(
                text = "Add Vendors",
                color = Color.White,
                style = MaterialTheme.typography.h6,
            )
        }
    }

//    LaunchedEffect(viewModel.selectedPincode.value) {
//        if (viewModel.selectedPincode.value.isNotEmpty()) {
//            //Toast.makeText(mContext, viewModel.toastError.value, Toast.LENGTH_SHORT).show()
//           // viewModel.selectedPincode.value = ""
//            viewModel.filterSuggestions()
//        }
//    }

}




