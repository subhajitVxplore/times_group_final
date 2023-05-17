package com.vxplore.thetimesgroup.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.core.utils.NavigationIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
internal fun NavHostController.NavEffects(
    navChannel: Channel<NavigationIntent>
) {
    val currentActivity = LocalContext.current as? Activity
    LaunchedEffect(currentActivity, this, navChannel) {

        navChannel.receiveAsFlow().collect { navIntent ->
            currentActivity?.isFinishing?.let {
                if (it) return@collect
            }

            when (navIntent) {
                is NavigationIntent.NavigateTo -> {
                    Log.d("BackStackEntry", "${this@NavEffects.currentBackStackEntry?.destination}")

                    navigate(navIntent.route) {
                        launchSingleTop = navIntent.isSingleTop
                        navIntent.popUpToRoute?.let {
                            popUpTo(it) {
                                inclusive = navIntent.inclusive
                            }
                        }
                    }
                }
                is NavigationIntent.NavigateBack -> {
                    if(navIntent.route != null) {
                        popBackStack(navIntent.route!!, navIntent.inclusive)
                    } else {
                        popBackStack()
                    }
                }
                else -> {

                }
            }
        }
    }

}