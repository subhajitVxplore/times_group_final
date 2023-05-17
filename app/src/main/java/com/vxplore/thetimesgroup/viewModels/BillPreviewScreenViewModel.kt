package com.vxplore.thetimesgroup.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BillPreviewScreenViewModel @Inject constructor(private val appNavigator: AppNavigator) :
    ViewModel() {


   // var bitmapImg :List<Bitmap>?=null
   var phoneNumber = mutableStateOf("6290353314")
   var message = mutableStateOf("Helloworld")

    // var bitmapImg: MutableState<Bitmap?> = mutableStateOf(null)
//    val images = remember {
//        mutableStateListOf<Bitmap>()
//    }
    fun onBillViewToBilling() {
        appNavigator.tryNavigateTo(
            route = Destination.Billing(),
            popUpToRoute = Destination.BillPreview(),
            isSingleTop = true,
            inclusive = true
        )
    }


}