package com.vxplore.thetimesgroup.viewModels

import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.model.Pincodes
import com.vxplore.core.domain.useCasess.AddVendorUseCases
import com.vxplore.thetimesgroup.custom_views.UiData
import com.vxplore.thetimesgroup.extensions.castListToRequiredTypes
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import com.vxplore.thetimesgroup.helpers_impl.SavableMutableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddVendorViewModel @Inject constructor(
    private val addVendorUseCases: AddVendorUseCases,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var visible by mutableStateOf(false)
    var vendorNameText = mutableStateOf("")
    var vendorMobileText = mutableStateOf("")
    var vendorEmailAddressText = mutableStateOf("")

    private val _pincodes = MutableStateFlow(emptyList<Pincodes>())
    val pincodes = _pincodes.asStateFlow()


    var selectedPincode = mutableStateOf("")
    var currentPincode = mutableStateOf("")

    private val _suggestionsss: MutableStateFlow<List<Pincodes>> = MutableStateFlow(emptyList())
    var suggestionListVisibility by mutableStateOf(false)
    private var suggestionsBackup: List<Pincodes> = emptyList()
    val isFocused = mutableStateOf(false)
    var mExpanded by mutableStateOf(false)

    val focusRequester = mutableStateOf(FocusRequester)


    val stateLoading = SavableMutableState(
        key = UiData.StateApiLoading,
        savedStateHandle = savedStateHandle,
        initialData = false
    )

    init {
        getPincodesByDistributorId()
    }

//    fun onAddVendorToAddVendorSuccess() {
//        appNavigator.tryNavigateTo(
//            route = Destination.AddVendorSuccess(),
//            // popUpToRoute = Destination.Dashboard(),
//            //isSingleTop = true,
//            //inclusive = true
//        )
//    }





    fun addVendor() {
        addVendorUseCases.addVendor(
            vendorNameText.value, vendorMobileText.value, vendorEmailAddressText.value, currentPincode.value
        ).flowOn(Dispatchers.IO).onEach {
            when (it.type) {
                EmitType.Navigate -> {
                    it.value?.apply {
                        castValueToRequiredTypes<String>()?.let { destination ->
                            appNavigator.tryNavigateTo(
                                //destination,
                                Destination.AddVendorSuccess(vendorNameText.value),
                                popUpToRoute = Destination.AddVendor.fullRoute,
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

    ///////////////////////////////////////////////////////////////////////////////////////////
    fun clearPincodesQuery() {
        selectedPincode.value = ""
        viewModelScope.launch {
            suggestionsBackup.apply {
                _pincodes.emit(this)
            }
        }
    }

    private fun getPincodesByDistributorId() {
        addVendorUseCases.getPincodesByDistributorId().flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                // pinCodeLoading.setValue(it)
                            }
                        }
                    }
                    EmitType.Pincodes -> {
                        it.value?.castListToRequiredTypes<Pincodes>()?.let { pincodes ->
                            _pincodes.update { pincodes }
                            suggestionsBackup = pincodes
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


    fun onSelectPincode(pincodes: Pincodes) {
        selectedPincode.value = currentPincode.value

        if (selectedPincode.value != "") {
            currentPincode.value = "${selectedPincode.value},${pincodes.pincode}"
        } else {
            currentPincode.value = pincodes.pincode
        }

        selectedPincode.value = ""
        visible = false
    }

    fun openPincodeSuggestions() {
        visible = !visible
    }

    fun findPincodes(query: String) {
        selectedPincode.value = query
        _pincodes.update {
            suggestionsBackup.filter { code ->
                code.pincode.contains(query.toRegex())
            }
        }
    }
}