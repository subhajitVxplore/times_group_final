package com.vxplore.thetimesgroup.custom_views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.vxplore.core.domain.model.SearchVendor
import com.vxplore.core.domain.model.SearchVendorModel
import com.vxplore.thetimesgroup.screens.Person


//val all = listOf("aaa", "baa", "aab", "abb", "bab")


var vendorList = mutableListOf<Person>()
val dropDownOptions = mutableStateOf(emptyList<String>())
val textFieldValue = mutableStateOf(TextFieldValue())
val dropDownExpanded = mutableStateOf(false)

@Composable
fun TextFieldWithDropdownUsage() {
    TextFieldWithDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue.value,
        setValue = ::onValueChanged,
        onDismissRequest = ::onDropdownDismissRequest,
        dropDownExpanded = dropDownExpanded.value,
        list = dropDownOptions.value,
    )
}

fun onDropdownDismissRequest() {
    dropDownExpanded.value = false
}


fun onValueChanged(value: TextFieldValue) {
    dropDownExpanded.value = true
    textFieldValue.value = value
    val all = listOf("aaa", "baa", "aab", "abb", "bab")
    //  val all: List<SearchVendor> = emptyList()
    //  val all: List<SearchVendor> = emptyList()
        dropDownOptions.value = all.filter {
            it.startsWith(value.text) && it != value.text
        }.take(3)
//    dropDownOptions.value = all.map {
//        if (it.name.lowercase() == value.text.lowercase()) it.name
//        else ""
//    }.filter { it.startsWith(value.text) && it != value.text}.take(3)
}




@Composable
fun TextFieldWithDropdown(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    setValue: (TextFieldValue) -> Unit,
    onDismissRequest: () -> Unit,
    dropDownExpanded: Boolean,
    list: List<String>,
) {
    Box(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .height(50.dp)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused)
                        onDismissRequest()
                },
            value = value,
            onValueChange = setValue,
            placeholder = { Text(text = "Vendor Name", color = Color.Gray) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            )

        )
        DropdownMenu(
            expanded = dropDownExpanded,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = onDismissRequest
        ) {
            list.forEach { text ->
                DropdownMenuItem(onClick = {
                    setValue(
                        TextFieldValue(
                            text,
                            TextRange(text.length)
                        )
                    )
                }) {
                    Text(text = text)
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////////////
//        BasicTextField(
//            value = value,
//            onValueChange = setValue,
//            //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
//            maxLines = 1,
//            modifier = Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 15.dp)
//                .onFocusChanged { focusState ->
//                    if (!focusState.isFocused)
//                        onDismissRequest()
//                },
//            //placeholder = { Text(text = "Vendor Name", color = Color.Gray)},
//            textStyle = TextStyle.Default.copy(fontSize = 20.sp)
//        ) {
//            TextFieldDefaults.OutlinedTextFieldDecorationBox(
//                value = value.toString(),
//                innerTextField = it,
//                enabled = true,
//                singleLine = true,
//                visualTransformation = VisualTransformation.None,
//                interactionSource = MutableInteractionSource(),
//                //contentPadding = PaddingValues(all = 4.dp),
//                contentPadding = PaddingValues(start = 10.dp),
//                placeholder = { Text(text = "Vendor Name", color = Color.Gray)},
//                colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = Color.White)
//            )
//        }