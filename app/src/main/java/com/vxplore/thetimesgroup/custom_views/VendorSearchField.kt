@file:OptIn(ExperimentalAnimationApi::class)

package com.vxplore.thetimesgroup.custom_views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vxplore.core.domain.model.SearchVendor
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel

//class MySearchField {}

@Composable
fun VendorSearchField(viewModel: BillingScreenViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        SearchVendorsSection(viewModel)
    }
}

@Composable
private fun RowScope.SearchVendorsSection(viewModel: BillingScreenViewModel) {

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = viewModel.searchVendorQuery,
                onValueChange = {
                    viewModel.updateVendorsQuery(it)
                },
                singleLine = true,
                trailingIcon = {
                    if (viewModel.searchVendorQuery.isNotEmpty()) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_close_24),
                            contentDescription = null, modifier = Modifier.clickable {
                                viewModel.clearVendorsQuery()
                            })
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null
                        )
                    }
                },
                placeholder = {
                    Text(text = "Search Vendor")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color(0xFF6650a4),
                    unfocusedBorderColor = Color(0xFF625b71),
                    cursorColor = Color.Transparent,
                    errorCursorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
            )
        }
    }
}

///////////////////////////////////////////////////////////////
@Composable
fun BoxScope.SuggestionsSection(
    suggestions: List<SearchVendor>,
    viewModel: BillingScreenViewModel
) {

    var visible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(initialAlpha = 0.4f),
        exit = fadeOut(animationSpec = tween(durationMillis = 250))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.LightGray
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {

                items(suggestions) { item ->
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 8.dp)
                            .clickable(onClick = {
                                viewModel.searchVendorQuery = item.name
                                viewModel.vendorId = item.user_id
                                viewModel.getPapersByVendorId(item.user_id)
                                visible = !visible
                            })
                    )
                }
            }
        }
    }

}


