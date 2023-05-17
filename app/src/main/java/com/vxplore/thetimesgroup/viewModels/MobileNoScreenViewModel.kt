package com.vxplore.thetimesgroup.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.useCasess.MobileNoUseCase
import com.vxplore.core.domain.useCasess.OtpUseCases
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MobileNoScreenViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val mobileNoUseCase: MobileNoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var MobileNoText = mutableStateOf("")
    val notifier = mutableStateOf("")
    var loadingButton = mutableStateOf(true)
    var loadingg = mutableStateOf(false)

    init {
       // Log.d("mobileNoTesting", "mobileNoTesting:++++")

        val number = savedStateHandle.get<String>(Destination.Otp.MOBILE_NO)
        number?.let {
            if(it != Destination.MobileNo.DUMMY_NUMBER) {
                MobileNoText.value = it
            }
        }
    }

    fun sendOtp(mobile_no: String) {
        mobileNoUseCase.sendOtp(mobile_no)
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.castValueToRequiredTypes<Boolean>()?.let {
                            loadingButton.value = it
                        }
                    }
                    EmitType.Navigate -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let { destination->
                                appNavigator.tryNavigateTo(
                                    destination,
                                    popUpToRoute = Destination.MobileNo.MOBILE_N,
                                    isSingleTop = true,
                                    inclusive = true
                                )
                            }
                        }
                    }
                    EmitType.INFORM -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let {
                                notifier.value = it
                            }
                        }
                    }
                    EmitType.BackendError -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let {
                                notifier.value = it
                            }
                        }
                    }
                    EmitType.NetworkError -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let {
                                notifier.value = it
                            }
                        }
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
    }





    // private var mobileNumber = "HelloWorld"
    fun onMobToOtp(mobileNumber: String) {
        appNavigator.tryNavigateTo(
            route = Destination.Otp(mobileNumber),
            popUpToRoute = Destination.MobileNo(mobileNumber),
            isSingleTop = true,
            inclusive = true
        )
    }

    fun exit() {
        //appNavigator.tryNavigateBack(route = Destination.Splash.fullRoute)
        appNavigator.tryNavigateBack()

        //appNavigator.navigateTo(destination(mobileNumber))
    }

}