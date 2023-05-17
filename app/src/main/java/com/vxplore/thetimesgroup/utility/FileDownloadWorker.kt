package com.vxplore.thetimesgroup.utility

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.custom_views.CustomDialog
import com.vxplore.thetimesgroup.ui.theme.GreenLight
import com.vxplore.thetimesgroup.viewModels.BillingScreenViewModel
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FileDownloadWorker(
    private val context: Context, workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val fileUrl = inputData.getString(FileParams.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: ""
        val fileType = inputData.getString(FileParams.KEY_FILE_TYPE) ?: ""

        Log.d("TAG", "doWork: $fileUrl | $fileName | $fileType")


        if (fileName.isEmpty() || fileType.isEmpty() || fileUrl.isEmpty()) {
            Result.failure()
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            val name = NotificationConstants.CHANNEL_NAME
//            val description = NotificationConstants.CHANNEL_DESCRIPTION
//            val importance = NotificationManager.IMPORTANCE_HIGH
////            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance)
////            channel.description = description
//
////            val notificationManager =
////                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
////
////            notificationManager?.createNotificationChannel(channel)
//
//        }

//        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("Downloading your file...").setOngoing(true).setProgress(0, 0, true)


        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return Result.failure()
        }
//        NotificationManagerCompat.from(context)
//            .notify(NotificationConstants.NOTIFICATION_ID, builder.build())

        val uri = getSavedFileUri(
            fileName = fileName, fileType = fileType, fileUrl = fileUrl, context = context
        )

        NotificationManagerCompat.from(context).cancel(NotificationConstants.NOTIFICATION_ID)
        return if (uri != null) {
            Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }

    }


    object FileParams {
        const val KEY_FILE_URL = "key_file_url"
        const val KEY_FILE_TYPE = "key_file_type"
        const val KEY_FILE_NAME = "key_file_name"
        const val KEY_FILE_URI = "key_file_uri"
    }

    object NotificationConstants {
        const val CHANNEL_NAME = "download_file_worker_demo_channel"
        const val CHANNEL_DESCRIPTION = "download_file_worker_demo_description"
        const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
        const val NOTIFICATION_ID = 1
    }

    fun getSavedFileUri(
        fileName: String, fileType: String, fileUrl: String, context: Context
    ): Uri? {
        val mimeType = when (fileType) {
            "PDF" -> "application/pdf"
            "PNG" -> "image/png"
            "MP4" -> "video/mp4"
            else -> ""
        } // different types of files will have different mime type

        if (mimeType.isEmpty()) return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/DownloaderDemo")
            }

            val resolver = context.contentResolver

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            return if (uri != null) {
                URL(fileUrl).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                uri
            } else {
                null
            }

        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            URL(fileUrl).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }

            return target.toUri()
        }
    }

}//end of class

data class MyFileModel(
    val id: String,
    val name: String,
    val type: String,
    val url: String,
    var downloadedUri: String? = null,
    var isDownloading: Boolean = false,
)

@Composable
fun ItemFile(
    viewModel: BillingScreenViewModel,
    startDownload: (MyFileModel) -> Unit,
) {

//    val showDialog = remember { mutableStateOf(false) }
//
//    if (showDialog.value)
//        CustomDialog(value = "", setShowDialog = {
//            showDialog.value = it
//        },
//            onClickPrintBill = {
//                viewModel.checkIfDeviceFound()
//            },
//            onClickViewBill = {
//                viewModel.onBillingToBillPreview()
//            }
//        ,viewModel)
//    val context = LocalContext.current
//    Button(
//        onClick = {
//           //viewModel.generateBillByJson()
//            showDialog.value = true
//        },
//        colors = ButtonDefaults.buttonColors(backgroundColor = GreenLight),
//        shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp),
//        enabled = viewModel.loadingBill.value,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp)
//
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = viewModel.generateBillButtonText,
//                fontSize = 17.sp,
//                color = Color.White,
//            )
//
//
//        }
//
//    }//Button


}



