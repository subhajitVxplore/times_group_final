package com.vxplore.thetimesgroup.mainController

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.caysn.autoreplyprint.AutoReplyPrint
import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.navigation.MainNavGraph
import com.vxplore.thetimesgroup.ui.theme.TheTimesGroupTheme
import com.vxplore.thetimesgroup.utility.FileDownloadWorker
import com.vxplore.thetimesgroup.utility.ItemFile
import com.vxplore.thetimesgroup.utility.MyFileModel
import com.vxplore.thetimesgroup.utility.printerService.TestUtils
import com.vxplore.thetimesgroup.viewModels.BaseViewModel
import com.vxplore.thetimesgroup.viewModels.BillPreviewScreenViewModel
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream


val client = HttpClient(Android) {
    engine {
        // this: AndroidEngineConfig
        connectTimeout = 100_000
        socketTimeout = 100_000
    }
}

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var mBluetoothAdapter: BluetoothAdapter? = null
    var mmDevice: BluetoothDevice? = null
    var isDeviceConnected: Boolean = false
    private val baseViewModel by viewModels<BaseViewModel>()

    // var bitmapImg:Bitmap?=null
    //end of class
    private var h: Pointer? = null
    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>



    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    //////////////////////onCreate/////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        super.onCreate(savedInstanceState)
        //findBTaddress()
        //  Toast.makeText(this,"BtAddress::::${mmDevice?.address}",Toast.LENGTH_SHORT).show()

        requestMultiplePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            var isGranted = false
            it.forEach { s, b ->
                isGranted = b
            }

            if (!isGranted) {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }







        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if(canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }

        setContent {
            TheTimesGroupTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = MaterialTheme.colors.background,
                ) { paddingValues ->
                    MainNavGraph(
                        navHostController = rememberNavController(),
                        navigationChannel = baseViewModel.appNavigator.navigationChannel,
                        paddingValues = paddingValues,
                        baseViewModel = baseViewModel
                    )
                }

            }
//            val context= LocalContext.current
//            ShowItemFileLayout(context)
        }
    }


    //////////////////////////////////////////////////////////////
    companion object {
        private var instance: MainActivity? = null

        @JvmStatic
        fun getInstance(): MainActivity? {
            return instance
        }
    }

    //------------------------------------------------------
    fun startDownloadingFile(
        file: MyFileModel,
        success: (String) -> Unit,
        failed: (String) -> Unit,
        running: () -> Unit
    ) {
        val data = Data.Builder()
        val workManager = WorkManager.getInstance(this)

        data.apply {
            putString(FileDownloadWorker.FileParams.KEY_FILE_NAME, file.name)
            putString(FileDownloadWorker.FileParams.KEY_FILE_URL, file.url)
            putString(FileDownloadWorker.FileParams.KEY_FILE_TYPE, file.type)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()


        workManager.enqueueUniqueWork(
            "oneFileDownloadWork_${System.currentTimeMillis()}",
            ExistingWorkPolicy.KEEP,
            fileDownloadWorker
        )

        workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
            .observe(this) { info ->
                info?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            success(
                                it.outputData.getString(FileDownloadWorker.FileParams.KEY_FILE_URI)
                                    ?: ""
                            )
                        }
                        WorkInfo.State.FAILED -> {
                            failed("Downloading failed!")
                        }
                        WorkInfo.State.RUNNING -> {
                            running()
                        }
                        else -> {
                            failed("Something went wrong")
                        }
                    }
                }
            }
    }

    //-------------------------------------------------------------------------
    @Composable
    fun ShowItemFileLayout(viewModel: BillingScreenViewModel, context: Context) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ItemFile(
                startDownload = { pdfFileModel ->
                    startDownloadingFile(
                        file = pdfFileModel,
                        success = {
                            Log.d("TESTING", "ShowItemFileLayout: $it")

                            /*pdfData.value = pdfData.value.copy().apply {
                                // isDownloading = false
                                viewModel.loadingBill.value = false
                                downloadedUri = it

                            }*/
                        },
                        failed = {
                            /*pdfData.value = pdfData.value.copy().apply {
                                viewModel.loadingBill.value = false
                                downloadedUri = null
                            }*/
                        },
                        running = {
                            /*pdfData.value = pdfData.value.copy().apply {
                                viewModel.loadingBill.value = true
                            }*/
                        }
                    )
                },
               /* openFile = {
                    try {
//------------------------------------------------------------------------------------------------
//                        val intent = Intent(Intent.ACTION_VIEW)
//                        intent.setDataAndType(it.downloadedUri?.toUri(), "application/pdf")
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        ContextCompat.startActivity(context, intent, null)
                        //---------------------------------------------------------------//
                        //  openPrinterPort()
                        convertPdfToBitmap(
                            target = viewModel.pdfData.value,
                            coroutineScope = lifecycleScope
                        ) {
                             tryToPrint(it)
                            // Log.d("ShowItemFileLayout", "ShowItemFileLayout: $it")
                            //Toast.makeText(this@MainActivity, "hello+++$it", Toast.LENGTH_SHORT).show()
                            baseViewModel.bitmapImg.clear()
                            baseViewModel.bitmapImg.addAll(it)
                            //  baseViewModel.bitmapImg=it.toMutableStateList()
                        }
                        billingScreenViewModel.onBillingToBillPreview()

                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "Can't open Pdf", Toast.LENGTH_SHORT).show()
                    }*/
                viewModel = viewModel
            )
        }


    }
    /////////////////////////print service////////////////////////





    private fun openPrinterPort() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            if (!mBluetoothAdapter!!.isEnabled()) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    startActivityForResult(enableBluetooth, 0)
                    return
                }
            }
            val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter!!.getBondedDevices()
            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {
                    mmDevice = device
                }
                isDeviceConnected = true
            }
            h = AutoReplyPrint.INSTANCE.CP_Port_OpenBtSpp("${mmDevice?.address}", 0)
            //h = AutoReplyPrint.INSTANCE.CP_Port_OpenBtSpp("86:67:7A:11:E4:93",0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }//openPrinterPort()

    /*override fun CP_OnPortOpenedEvent(p0: Pointer?, p1: String?, p2: Pointer?) {
        lifecycleScope.launchWhenCreated {
            Toast.makeText(
                this@MainActivity,
                "Status Pointer1 ${p0.toString()} Name: $p1 Pointer2 ${p2.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }*/


    @Composable
    fun VendorBillPreviewScreen(
        viewModel: BillPreviewScreenViewModel = hiltViewModel(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "back button",
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            )


        }


    }


}
