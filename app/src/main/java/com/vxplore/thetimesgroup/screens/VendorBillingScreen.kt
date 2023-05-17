@file:Suppress("DEPRECATION")

package com.vxplore.thetimesgroup.screens

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.caysn.autoreplyprint.AutoReplyPrint
import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.custom_views.*
import com.vxplore.thetimesgroup.mainController.MainActivity
import com.vxplore.thetimesgroup.mainController.client
import com.vxplore.thetimesgroup.ui.theme.DonutGreenLight
import com.vxplore.thetimesgroup.ui.theme.GreyLight
import com.vxplore.thetimesgroup.utility.printerService.TestUtils
import com.vxplore.thetimesgroup.viewModels.BaseViewModel
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun VendorBillingScreen(
    viewModel: BillingScreenViewModel = hiltViewModel(),
    baseViewModel: BaseViewModel,
) {
//    var coupon_total = 0

    val suggestions = viewModel.suggestion.collectAsState().value
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // .wrapContentHeight()
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
                text = "Billing Now",
                color = Color.DarkGray,
                fontSize = 17.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.TopEnd
            ) {
                Button(
                    onClick = { viewModel.onBillingToAddVendor() },
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = DonutGreenLight)
                ) {
                    Text(
                        text = "Add Vendor",
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontStyle = FontStyle.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(7.dp))
        VendorSearchField(viewModel)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            //if (viewModel.searchVendorQuery != "") {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(7.dp))
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    showPapersTakenList(
                        viewModel.circleLoading.value,
                        viewModel.paperss.collectAsState().value,
                        viewModel
                    ) { value2, index, value1 ->
                        try {
                            // viewModel.takenPapers[index] =Pair(first = value1, second = value2.toInt())
                            //viewModel.takenPapers.add(index, Pair(first = value1, second = value2.toInt()))
                            viewModel.calculateTakenPapersPrice(value1, value2.toFloat(), index)
                        } catch (e: NumberFormatException) {
                            viewModel.calculateTakenPapersPrice(value1, 0f, index)
                            println(e)
                        } catch (e: Exception) {
                            println(e)
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))

                    val paperssListing = viewModel.paperss.collectAsState().value
                    if (paperssListing.isNotEmpty()) {
                        ExpandableCardReturnPapers(
                            viewModel.circleLoading.value,
                            "Returns",
                            viewModel.paperss.collectAsState().value,
                            viewModel
                        ) { value2, index, value1 ->
                            try {
                                viewModel.calculateReturnPapersPrice(
                                    value1,
                                    value2.toFloat(),
                                    index
                                )
                            } catch (e: NumberFormatException) {
                                viewModel.calculateReturnPapersPrice(value1, 0f, index)
                                println(e)
                            } catch (e: Exception) {
                                println(e)
                            }
                        }
                    }

                    if (paperssListing.isNotEmpty()) {
                        ExpandableCardCoupons(
                            viewModel.circleLoading.value,
                            "Coupons",
                            viewModel.couponss.collectAsState().value,
                            viewModel
                        ) { value, index, multi ->
                            try {
                                viewModel.calculateCouponPrice(multi, value.toInt(), index)
                            } catch (e: NumberFormatException) {
                                viewModel.calculateCouponPrice(multi, 0, index)
                                println(e)
                            } catch (e: Exception) {
                                println(e)
                            }
                        }
                    }


                }
                //----------------Bottom Layout-------------------//
                Column(
                    modifier = Modifier
                        .weight(0.4f, true)
                ) {
                    if (viewModel.paperss.collectAsState().value.isNotEmpty()) {
                        BillingScreenBottomLayout(viewModel)
                    }
                }

            }
            //   }//column
            ///suggestion dropdown section calling
            AnimatedContent(targetState = suggestions.isNotEmpty()) { state ->
                if (viewModel.searchVendorQuery != "") {
                    when (state) {
                        true -> SuggestionsSection(suggestions, viewModel)
                        false -> Surface() { state }
                    }
                }
            }
        }//box

    }//parent column
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    if (viewModel.scannedBLdevices.isNotEmpty()) {
        Dialog(onDismissRequest = { viewModel.scannedBLdevices.clear() }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                LazyColumn(
                    modifier = Modifier
                        .width(screenWidth * .90f)
                        .height(screenHeight * .70f)
                ) {
                    items(viewModel.scannedBLdevices) { device ->
                        Card(
                            modifier = Modifier.wrapContentWidth(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                viewModel.onClickDevice(device)
                            }) {
                            device.name?.let {
                                Text(text = it, modifier = Modifier.padding(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }


    val mContext = LocalContext.current
    LaunchedEffect(key1 = viewModel.printerFound) {
        viewModel.printerFound?.let {
            AutoReplyPrint.INSTANCE.CP_Port_AddOnPortOpenedEvent(
                viewModel,
                Pointer.NULL
            )
            AutoReplyPrint.INSTANCE.CP_Port_OpenBtSpp(it.address, 0)?.let { p ->
                viewModel.setPrinterPointer(p)
            }

            viewModel.printerFound = null
        }
    }

    LaunchedEffect(viewModel.pdfData.value) {
        viewModel.pdfData.value.let {
            if (it.isNotEmpty()) {
                baseViewModel.billUrl.value = it
                baseViewModel.vendorWhatsappNo.value = viewModel.vendorPhone.value
                try {
                    mContext.convertPdfToBitmap(
                        target = it,
                        coroutineScope = (mContext as MainActivity).lifecycleScope,
                    ) { bitmaps, pdf ->
                        viewModel.pPointer?.let { it1 -> mContext.tryToPrint(it1, bitmaps) }
                        baseViewModel.addBitmaps(bitmaps)
                        baseViewModel.pdfBill = pdf
                        viewModel.onBillingToBillPreview()
                    }
                    viewModel.pdfData.value = ""
                } catch (ex: Exception) {
                    Log.d("TESTING", "MESSAGE ${ex.message}")
                }
            }
        }
    }


    LaunchedEffect(key1 = viewModel.printerConnectedNotify) {
        viewModel.printerConnectedNotify.let {
            if (it.isNotEmpty()) {
                Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
                viewModel.printerConnectedNotify = ""
            }
        }
    }

    LaunchedEffect(viewModel.toastError.value) {
        if (viewModel.toastError.value.isNotEmpty()) {
            Toast.makeText(mContext, viewModel.toastError.value, Toast.LENGTH_SHORT).show()
            viewModel.toastError.value = ""
        }
    }

    LaunchedEffect(viewModel.takenPapersTotal.value, viewModel.returnsTotal.value) {
        viewModel.takenMinusreturnPaperTotal.value =
            viewModel.takenPapersTotal.value - viewModel.returnsTotal.value
        //viewModel.takenPapersTotal.value=0
    }

    LaunchedEffect(viewModel.cashPayment.value, viewModel.couponsTotal.value) {
        viewModel.cashMinusCouponTotal.value =
            viewModel.cashPayment.value + viewModel.couponsTotal.value

    }

    LaunchedEffect(
        viewModel.takenMinusreturnPaperTotal.value,
        viewModel.cashMinusCouponTotal.value,
        viewModel.previousDue.value
    ) {
        //if (viewModel.takenMinusreturnPaperTotal.value > 0) {
        viewModel.currentDue.value =
            viewModel.previousDue.value.plus(viewModel.takenMinusreturnPaperTotal.value)
        // }
    }
    LaunchedEffect(
        viewModel.currentDue.value,
        viewModel.cashMinusCouponTotal.value
    ) {
        //if (viewModel.takenMinusreturnPaperTotal.value > 0) {
        viewModel.balanceAmount.value =
            viewModel.currentDue.value - viewModel.cashMinusCouponTotal.value
        // }
    }


}//VendorBillingScreen


private fun Context.tryToPrint(requiredPointer: Pointer, bitmaps: List<Bitmap>) {
    bitmaps.forEach { bitmap ->
        bitmap.apply {
            var printwidth = 384
            val width_mm = IntByReference()
            val height_mm = IntByReference()
            val dots_per_mm = IntByReference()

            Thread {
                try {
                    if (!AutoReplyPrint.INSTANCE.CP_Printer_GetPrinterResolutionInfo(
                            requiredPointer,
                            width_mm,
                            height_mm,
                            dots_per_mm
                        )
                    ) {
                        printwidth = width_mm.value * dots_per_mm.value
                    }

                    val nh = (height * (384 / width))
                    val scaled = TestUtils.scaleImageToWidth(
                        this@apply,
                        printwidth
                    ) //Bitmap.createScaledBitmap(this@apply, 500, height_mm.value, true)

                    val result =
                        AutoReplyPrint.CP_Pos_PrintRasterImageFromData_Helper.PrintRasterImageFromBitmap(
                            requiredPointer,
                            scaled.width,
                            scaled.height,
                            scaled,
                            AutoReplyPrint.CP_ImageBinarizationMethod_ErrorDiffusion,
                            AutoReplyPrint.CP_ImageCompressionMethod_None
                        )
                    //  billPreviewScreenViewModel.bitmapImg=scaled

                    if (!result) TestUtils.showMessageOnUiThread(
                        this@tryToPrint as MainActivity,
                        "Write failed"
                    )
                    //Toast.makeText(this@MainActivity, "$result", Toast.LENGTH_SHORT).show()
                    AutoReplyPrint.INSTANCE.CP_Pos_Beep(requiredPointer, 1, 500)
                    AutoReplyPrint.INSTANCE.CP_Port_Close(requiredPointer)
                } catch (ex: Exception) {
                    Log.d("PRINT ERROR", "tryToPrint: ${ex.message}")
                }
            }.start()
        }
    }

}

private fun Context.convertPdfToBitmap(
    target: String = "",
    coroutineScope: CoroutineScope,
    onBitmapCreated: (List<Bitmap>, File) -> Unit,
) {
    val bitmaps = mutableListOf<Bitmap>()
    coroutineScope.launch {
        try {
            val inputStream = client.get(urlString = target).body<InputStream>()
            val path = kotlin.io.path.createTempFile(prefix = "sample", suffix = ".pdf")
            inputStream.use { input ->
                path.outputStream().use {
                    input.copyTo(it)
                }
            }
            val file = File(path.absolutePathString())
            Log.d(
                "TESTING",
                "File ${path.absolutePathString()} ${file.isFile} ${file.name} ${file.path}"
            )
            val pdfRenderer = PdfRenderer(
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            )
            repeat(pdfRenderer.pageCount) { idx ->
                val page = pdfRenderer.openPage(idx)
                val w = resources.displayMetrics.densityDpi / 72 * page.width
                val h = resources.displayMetrics.densityDpi / 72 * page.height
                val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
                val newBitmap = Bitmap.createBitmap(
                    bitmap.width,
                    bitmap.height, bitmap.config
                )
                val canvas = android.graphics.Canvas(newBitmap)
                canvas.drawColor(android.graphics.Color.WHITE)
                canvas.drawBitmap(bitmap, 0f, 0f, Paint())
                newBitmap?.let {
                    bitmaps.add(it)

                    //   billPreviewScreenViewModel.bitmapImg=it
                }
                page.close()
            }
            onBitmapCreated(bitmaps, file)
            pdfRenderer.close()
        } catch (ex: Exception) {
            Toast.makeText(
                this@convertPdfToBitmap,
                "${ex.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}




