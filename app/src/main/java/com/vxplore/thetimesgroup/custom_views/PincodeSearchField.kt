package com.vxplore.thetimesgroup.custom_views

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.extensions.*
import com.vxplore.thetimesgroup.viewModels.AddVendorViewModel
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PincodeSearchField(viewModel: AddVendorViewModel) {

    val icon = if (viewModel.visible) Icons.Filled.KeyboardArrowUp
    else Icons.Filled.Add

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                Row(
                    modifier = Modifier
                        //  .clickable { mExpanded = !mExpanded }
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = viewModel.currentPincode.value,
                        color = Color.DarkGray,
                        fontSize = 17.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Icon(
                    icon,
                    "contentDescription",
                    Modifier
                        .padding(end = 7.dp)
                        .align(Alignment.CenterVertically)
                        .clickable(onClick = viewModel::openPincodeSuggestions)
                )
            }
        }
    }

    // }
}/////----->

////////////////////////////////////////////////////////////////////////////
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchPincodesSection(viewModel: AddVendorViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, Color.Gray),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(33.dp),
                value = viewModel.selectedPincode.value,
                onValueChange = viewModel::findPincodes,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                ),
                maxLines = 1,
                textStyle = TextStyle(fontSize = 18.sp)
            ) {
                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                    value = viewModel.selectedPincode.value,
                    innerTextField = it,
                    placeholder = { Text(text = "Serving PinCode(s)") },
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = MutableInteractionSource(),
                    // contentPadding = PaddingValues(vertical = -5.dp),
                    contentPadding = PaddingValues(start = 10.dp),
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        backgroundColor = Color.White
                    ),

                    )
            }

            if (viewModel.selectedPincode.value.isNotEmpty()) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_close_24),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(.2f)
                        .clickable {
                            viewModel.clearPincodesQuery()
                        }
                        .align(Alignment.CenterVertically))
            } else {
                Icon(
                    modifier = Modifier
                        .weight(.2f)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }

        }
    }

}

///////////////////////////////////////////////////////////////
@Composable
fun PincodesSuggestionsSection(viewModel: AddVendorViewModel) {
    val pncodes = viewModel.pincodes.collectAsState().value
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.LightGray
    ) {
        Column() {
            SearchPincodesSection(viewModel)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)

            ) {
                items(pncodes) { item ->
                    Text(
                        text = item.pincode,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 15.dp, bottom = 10.dp)
                            .clickable(onClick = {
                                viewModel.onSelectPincode(item)
                                focusManager.clearFocus()
                            })
                    )
                }
            }
        }//column
    }
}




