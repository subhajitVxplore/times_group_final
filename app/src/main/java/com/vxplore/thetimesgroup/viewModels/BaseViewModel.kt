package com.vxplore.thetimesgroup.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.core.utils.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    val appNavigator: AppNavigator,
) : ViewModel() {

    var contextt = mutableStateOf(LocalContext)
    var bitmapImages = mutableStateListOf<Bitmap>()
    // var bitmapImg = mutableStateListOf<Bitmap>()
    // var bitmapImg: MutableState<Bitmap?> = mutableStateOf(null)

    var phoneNumber= mutableStateOf("7003103258")
    var message= mutableStateOf("Hello World")
    var billUrl= mutableStateOf("")
    var addedVendorName= mutableStateOf("")

    lateinit var pdfBill:File

    var vendorWhatsappNo= mutableStateOf("")
    var mobileNoClearText= mutableStateOf("")


    fun addBitmaps(bitmaps: List<Bitmap>) {
        bitmapImages.clear()
        bitmapImages.addAll(bitmaps)
    }



}