package com.vxplore.thetimesgroup.screens

import android.content.Intent
import android.net.Uri
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vxplore.core.common.AppRoutes
import com.vxplore.core.domain.model.AppVersion
import com.vxplore.thetimesgroup.R
import com.vxplore.thetimesgroup.extensions.MyDialog
import com.vxplore.thetimesgroup.extensions.OnEffect
import com.vxplore.thetimesgroup.viewModels.SplashViewModel
import kotlinx.coroutines.delay
import java.util.Properties

@Composable
//fun SplashScreen(navController: NavController) {
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    // AnimationEffect
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(3000L)
        //navController.navigate(AppRoutes.MOBILE_NO)
        //viewModel.onSplashRun()
    }

    // Image
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.the_times_group_logo),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value))
    }


    viewModel.versionUpdateDialog.value?.apply {
        if (currentState()) {
            AlertDialog(
                properties= DialogProperties(dismissOnClickOutside = false),
                shape = RoundedCornerShape(10.dp),
                onDismissRequest = {
                    onDismiss?.invoke(null)
//                    if ((currentData?.data as AppVersion).isSkipable) {
//                        setState(MyDialog.Companion.State.DISABLE)
//                    }
                },
                buttons = {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if ((currentData?.data as AppVersion).isSkipable=="NO") {
                            currentData?.negative?.let {
                                OutlinedButton(
                                    modifier = Modifier.padding(10.dp),
                                    onClick = {
                                        onDismiss?.invoke(null)
                                              },
                                    shape = RoundedCornerShape(30),
                                    border = BorderStroke(1.dp, color = Color.Gray,
                                   )
                                ) {
                                    Text(text = it, color = Color.Gray) //updateLater  btn text
                                }
                            }
                        }
                        currentData?.positive?.let {
                            OutlinedButton(
                                onClick = { onConfirm?.invoke(currentData?.data) },
                                shape = RoundedCornerShape(30),
                                border = BorderStroke(1.dp, color = Color.Blue),
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(text = it, color = Color.Blue) //updateNow  btn text
                            }
                        }
                    }
                },
                modifier = Modifier,
                title = {
                    currentData?.title?.let {
                        Text(text = it, ) //version update title
                    }

                },
                text = {
                    currentData?.message?.let {
                        Text(text = it, ) //version message from Api
                    }
                }
            )
        }
    }
    EffectHandlers(viewModel)
}

@Composable
private fun EffectHandlers(viewModel: SplashViewModel) {
    val localContext = LocalContext.current

    viewModel.versionUpdateLink.OnEffect(
        intentionalCode = { link->
            link?.let { lk->
                if(lk.isNotEmpty()) {
                    try {
                        val appStoreIntent = Intent(Intent.ACTION_VIEW).also {
                            it.data = Uri.parse(lk)
                        }
                        localContext.startActivity(appStoreIntent)
                    } catch (exception: Exception) {
                       // Toast.makeTextl(localContext, "unable_to_open_play_store", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        },
        clearance = {""}
    )
}



