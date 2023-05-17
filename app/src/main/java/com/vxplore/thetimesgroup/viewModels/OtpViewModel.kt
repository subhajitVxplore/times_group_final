package com.vxplore.thetimesgroup.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.useCasess.OtpUseCases
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val otpUseCases: OtpUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var loadingButton = mutableStateOf(true)
    var loadingg = mutableStateOf(false)
    val number = savedStateHandle.get<String>(Destination.MobileNo.MOBILE_N)
   // fun onOtpToMob(mobileNumber: String) {
    fun onOtpToMob() {

        Log.d("MOBILE", number.toString())
        appNavigator.tryNavigateTo(
            route = Destination.MobileNo(number.toString()),
            popUpToRoute = Destination.Otp(number.toString()),
            isSingleTop = true,
            inclusive = true
        )
      //  number=""
    }

    fun verifyOtp(otpp: String) {
        otpUseCases.verifyOtp(number, otpp).flowOn(Dispatchers.IO).onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.castValueToRequiredTypes<Boolean>()?.let {
                            loadingButton.value = it
                        }
                    }
                    EmitType.Navigate -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let { destination ->
                                appNavigator.tryNavigateTo(
                                    destination,
                                    popUpToRoute = Destination.Otp.fullRoute,
                                    isSingleTop = true,
                                    inclusive = true
                                )
                            }
                        }
                    }

                    EmitType.NetworkError -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let {

                            }
                        }
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
    }


}