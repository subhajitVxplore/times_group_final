package com.vxplore.thetimesgroup.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.core.utils.NavigationIntent
import com.vxplore.core.common.Destination
import com.vxplore.thetimesgroup.screens.*
import com.vxplore.thetimesgroup.viewModels.BaseViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    navigationChannel: Channel<NavigationIntent>,
    paddingValues: PaddingValues,
    baseViewModel: BaseViewModel
) {
    navHostController.NavEffects(navigationChannel)

    AppNavHost(
        navController = navHostController,
        startDestination = Destination.Splash,
        modifier = Modifier.padding(paddingValues),
        enterTransition = AppScreenTransitions.ScreenEnterTransition,
        popEnterTransition = AppScreenTransitions.ScreenPopEnterTransition,
        exitTransition = AppScreenTransitions.ScreenExitTransition,
        popExitTransition = AppScreenTransitions.ScreenPopExitTransition,
    ) {
        composable(destination = Destination.Splash) {
           // SplashScreen(navHostController)
            SplashScreen()
        }
        composable(destination = Destination.MobileNo) {
            // MobileNoScreen(onContinueClick = {navHostController.navigate(AppRoutes.OTP)})
            MobileNoScreen(navController = navHostController)//
        }
        composable(destination=Destination.Otp){
            OtpScreen()
        }
        composable(destination=Destination.Dashboard){
            DashboardContainerScreen()
        }
        composable(destination=Destination.Register){
            RegistrationScreen()
        }
        composable(destination = Destination.Billing) {
            VendorBillingScreen(baseViewModel = baseViewModel)
        }
        composable(destination = Destination.BillPreview) {
            VendorBillPreViewScreen(baseViewModel = baseViewModel)
        }

        composable(destination = Destination.AddVendor) {
            AddVendorScreen(baseViewModel = baseViewModel)
        }
        composable(destination = Destination.AddVendorSuccess) {
            VendorAddSuccessfulScreen(baseViewModel = baseViewModel)
        }


    }
}