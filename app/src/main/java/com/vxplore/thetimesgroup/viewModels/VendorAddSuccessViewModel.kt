package com.vxplore.thetimesgroup.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class VendorAddSuccessViewModel @Inject constructor(private val appNavigator: AppNavigator,savedStateHandle: SavedStateHandle) :
    ViewModel() {


    var vendor = savedStateHandle.get<String>(Destination.AddVendorSuccess.ADDED_VENDORR)

    var vendorNameAdded= mutableStateOf(vendor)

    fun onAddVendorSuccessToAddVendor() {
        appNavigator.tryNavigateTo(
            route = Destination.AddVendor(),
            popUpToRoute = Destination.AddVendorSuccess(""),
            isSingleTop = true,
            inclusive = true
        )
    }

    fun onAddVendorSuccessToDashboard() {
        appNavigator.tryNavigateBack(
            route = Destination.Dashboard(),
//            popUpToRoute = Destination.AddVendorSuccess(),
//            isSingleTop = true,
            //inclusive = true
        )
    }
}