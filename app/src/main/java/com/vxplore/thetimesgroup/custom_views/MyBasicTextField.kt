package com.vxplore.thetimesgroup.custom_views

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyBasicTextField(
    text: String = "",
    onValueChange: (String) -> Unit
) {
    var value by remember(text) {
        mutableStateOf(text)
    }

    BasicTextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(value)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        maxLines = 1,
        textStyle = TextStyle(fontSize = 18.sp,fontWeight = FontWeight.Bold),
    ) {
        TextFieldDefaults.OutlinedTextFieldDecorationBox(
            value = value,
            innerTextField = it,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = MutableInteractionSource(),
           // contentPadding = PaddingValues(all = 4.dp),
            contentPadding = PaddingValues(start = 10.dp),
//            colors = ExposedDropdownMenuDefaults.textFieldColors(
//                backgroundColor = Color.White
//            ),

        )
    }
}

