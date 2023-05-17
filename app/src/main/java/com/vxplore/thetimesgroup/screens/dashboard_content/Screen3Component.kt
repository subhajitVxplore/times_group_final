package com.vxplore.thetimesgroup.screens.dashboard_content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Screen3Component(openDrawer: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(color = Color(0xFFfffbd0.toInt()), modifier = Modifier.weight(1f)) {

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "Screen 3")
                   // LazyColumnItemsScrollableComponent(getPersonList())
                }
            )
        }
    }
}