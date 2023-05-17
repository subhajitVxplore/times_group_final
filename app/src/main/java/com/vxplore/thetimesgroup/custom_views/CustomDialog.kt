package com.vxplore.thetimesgroup.custom_views

import android.R
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel


@Composable
fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, onClickPrintBill: ()->Unit, onClickViewBill: ()->Unit,viewModel: BillingScreenViewModel) {

    Dialog(onDismissRequest = { setShowDialog(true) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vendor Bill Generated",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                      /*  val showDialog =  remember { mutableStateOf(false) }

                        if(showDialog.value)
                            BluetoothDevicesDialog(setShowDialog = {
                                showDialog.value = it
                            }, viewModel=viewModel)
*/

                        Column(modifier = Modifier
                           .clickable(onClick = onClickPrintBill)
                        ) {
                            Image(
                                painter = painterResource(id = com.vxplore.thetimesgroup.R.drawable.baseline_print_24),
                                contentDescription = "Print Bill",
                                modifier = Modifier
                                    .height(70.dp)
                                    .width(100.dp)
                                    .align(Alignment.CenterHorizontally)
                                    //.padding(horizontal = 10.dp)
                            )

                            Text(
                                text = "Print Bill",
                                color = Color.DarkGray,
                                fontSize = 15.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        Column(modifier = Modifier
                            .clickable(onClick = onClickViewBill)
                        ) {
                            Image(
                                painter = painterResource(id = com.vxplore.thetimesgroup.R.drawable.outline_remove_red_eye_24),
                                contentDescription = "View Bill",
                                modifier = Modifier
                                    .height(70.dp)
                                    .width(100.dp)
                                    .align(Alignment.CenterHorizontally)
                                   // .padding(horizontal = 10.dp)
                            )

                            Text(
                                text = "View/Share Bill",
                                color = Color.DarkGray,
                                fontSize = 15.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }


                    }


                }
            }
        }
    }
}