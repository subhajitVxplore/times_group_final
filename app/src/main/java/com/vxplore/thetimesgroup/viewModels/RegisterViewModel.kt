package com.vxplore.thetimesgroup.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.model.District
import com.vxplore.core.domain.model.Pincode
import com.vxplore.core.domain.model.State
import com.vxplore.core.domain.useCasess.RegisterUsecases
import com.vxplore.thetimesgroup.custom_views.UiData
import com.vxplore.thetimesgroup.extensions.castListToRequiredTypes
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import com.vxplore.thetimesgroup.helpers_impl.SavableMutableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUsecases: RegisterUsecases,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val number = savedStateHandle.get<String>(Destination.Otp.MOBILE_NO)

    var loadingButton = mutableStateOf(true)
    var loadingg = mutableStateOf(false)

    var yourNameText = mutableStateOf("")
    var emailAddressText = mutableStateOf("")
    var addressText = mutableStateOf("")

    private val _states = MutableStateFlow(emptyList<State>())
    val states = _states.asStateFlow()
    var selectedState = mutableStateOf("")
    var loadedState = mutableStateListOf<State>()

    private val _districts = MutableStateFlow(emptyList<District>())
    val districts = _districts.asStateFlow()
    var selectedDistrict = mutableStateOf("")


    private val _pincodes = MutableStateFlow(emptyList<Pincode>())
    val pincodes = _pincodes.asStateFlow()
    var selectedPincode = mutableStateOf("")


    init {
        getAllStates()
        getDistrictByState(selectedDistrict.value)
        getPincodeByDistrict(selectedPincode.value)
    }


    val stateLoading = SavableMutableState(
        key = UiData.StateApiLoading,
        savedStateHandle = savedStateHandle,
        initialData = false
    )

    val districtLoading = SavableMutableState(
        key = UiData.DistrictApiLoading,
        savedStateHandle = savedStateHandle,
        initialData = false
    )

    val pinCodeLoading = SavableMutableState(
        key = UiData.PincodeApiLoading,
        savedStateHandle = savedStateHandle,
        initialData = false
    )

    fun getAllStates() {
        registerUsecases.getAllState()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                stateLoading.setValue(it)
                            }
                        }
                    }

                    EmitType.States -> {
                        it.value?.castListToRequiredTypes<State>()?.let { states ->
                            _states.update { states }
                            loadedState.addAll(states)
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

    fun getDistrictByState(state: String) {
        registerUsecases.getDistrictByState(state)
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                districtLoading.setValue(it)
                            }
                        }
                    }

                    EmitType.Districts -> {
                        it.value?.castListToRequiredTypes<District>()?.let { districts ->
                            _districts.update { districts }
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
            }
            .launchIn(viewModelScope)
    }


    fun getPincodeByDistrict(district: String) {
        registerUsecases.getPincodeByDistrict(district)
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                pinCodeLoading.setValue(it)
                            }
                        }
                    }
                    EmitType.Pincodes -> {
                        it.value?.castListToRequiredTypes<Pincode>()?.let { pincodes ->
                            _pincodes.update { pincodes }
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
            }
            .launchIn(viewModelScope)
    }


    fun register() {
        registerUsecases.register(yourNameText.value,emailAddressText.value,number.toString(),addressText.value,selectedState.value,selectedDistrict.value,selectedPincode.value)
            .flowOn(Dispatchers.IO).onEach {
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
                                popUpToRoute = Destination.Register.fullRoute,
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