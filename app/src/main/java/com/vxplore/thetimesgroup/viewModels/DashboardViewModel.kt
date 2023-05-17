package com.vxplore.thetimesgroup.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import com.vxplore.core.common.DialogData
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.model.*
import com.vxplore.core.domain.useCasess.DashboardUseCases
import com.vxplore.thetimesgroup.custom_views.UiData
import com.vxplore.thetimesgroup.extensions.MyDialog
import com.vxplore.thetimesgroup.extensions.castListToRequiredTypes
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import com.vxplore.thetimesgroup.helpers_impl.SavableMutableState
import com.vxplore.thetimesgroup.screens.PaperSold
import com.vxplore.thetimesgroup.utility.bluetoothService.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashBoardUseCases: DashboardUseCases,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _vendors = MutableStateFlow(emptyList<Vendor>())
    val vendors = _vendors.asStateFlow()

    private val _paperCodes = MutableStateFlow(emptyList<PaperCode>())
    val paperCodes = _paperCodes.asStateFlow()
    var totalPapersSold = mutableStateOf("")

    var todayPaperSold = mutableStateOf("")
    var todayEachPaperSold = mutableStateOf("")
    var thisMonthPaperSold = mutableStateOf("")

    var todayPaperReturn = mutableStateOf("")
    var todayEachPaperReturn = mutableStateOf("")
    var thisMonthPaperReturn = mutableStateOf("")
    var distributorName = mutableStateOf("")
    var distributorId = mutableStateOf("")


    val dashboardBack = mutableStateOf<MyDialog?>(null)
    var values = listOf<PaperSold>()
    var sumOfValues = mutableStateOf(0f)

    // values: List<Float> = listOf(10f, 20f, 30f,40f,50f),
    //val vendorsQuery = mutabletateOf("")
    init {
        getTodayPaperSoldByUserId()
        getVendors()
        calculateDonutSweepAngles()
        getDonutChartData()
        getDistributorDetails()
    }

    fun calculateDonutSweepAngles() {
        values.forEach {
            sumOfValues.value += it.floatValue

        }

    }

    fun onDashboardToBilling() {


        appNavigator.tryNavigateTo(
            route = Destination.Billing(),
            // popUpToRoute = Destination.Dashboard(),
            //isSingleTop = true,
            //inclusive = true
        )
    }

    val vendorsLoading = SavableMutableState(
        key = UiData.LoginApiLoading,
        savedStateHandle = savedStateHandle,
        initialData = false
    )


    fun onDashboardExit() {
        appNavigator.tryNavigateTo(
            route = Destination.Billing(),
            popUpToRoute = Destination.Dashboard(),
            isSingleTop = true,
            inclusive = true
        )
    }


    fun getTodayPaperSoldByUserId() {
        dashBoardUseCases.getTodayPaperSoldByUserId()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                //districtLoading.setValue(it)
                            }
                        }
                    }

                    EmitType.PaperSold -> {
                        it.value?.castValueToRequiredTypes<com.vxplore.core.domain.model.PaperSold>()
                            ?.let {
                                todayPaperSold.value = it.todays_total.toString()
                                todayEachPaperSold.value = it.each_paper_sold
                                thisMonthPaperSold.value = it.this_month.toString()
                            }
                    }

                    EmitType.PaperReturn -> {
                        it.value?.castValueToRequiredTypes<PaperReturn>()?.let {
                            todayPaperReturn.value = it.todays_total.toString()
                            todayEachPaperReturn.value = it.each_paper_return
                            thisMonthPaperReturn.value = it.this_month.toString()
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

    fun getVendors() {
        dashBoardUseCases.getVendors()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                vendorsLoading.setValue(it)
                            }
                        }
                    }
                    EmitType.VENDORS -> {
                        it.value?.castListToRequiredTypes<Vendor>()?.let { vendors ->
                            _vendors.update { vendors }
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


    fun getDonutChartData() {
        dashBoardUseCases.getDonutChartData()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                vendorsLoading.setValue(it)
                            }
                        }
                    }
                    EmitType.PAPER_CODE -> {
                        it.value?.castListToRequiredTypes<PaperCode>()?.let { paperCodess ->
                            _paperCodes.update { paperCodess }
                        }
                    }

                    EmitType.TOTAL_SOLD_PAPER -> {
                        it.value?.castValueToRequiredTypes<String>()?.let {
                            totalPapersSold.value = it
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


    fun getDistributorDetails() {
        dashBoardUseCases.getDistributorDetails()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
//                    EmitType.Loading -> {
//                        it.value?.apply {
//                            castValueToRequiredTypes<Boolean>()?.let {
//                                vendorsLoading.setValue(it)
//                            }
//                        }
//                    }
                    EmitType.DISTRIBUTOR_NAME -> {
                        it.value?.castValueToRequiredTypes<String>()?.let {
                            distributorName.value = it
                        }
                    }
                    EmitType.DISTRIBUTOR_ID -> {
                        it.value?.castValueToRequiredTypes<String>()?.let {
                            distributorId.value = it
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


    fun onBackDialog() {
        dashboardBack.value = MyDialog(
            data = DialogData(
                title = "The TimesGroup",
                message = "Are you sure you want to exit ?",
                positive = "Yes",
                negative = "No",
            )
        )
        handleDialogEvents()
    }


    private fun handleDialogEvents() {
        dashboardBack.value?.onConfirm = {

        }
        dashboardBack.value?.onDismiss = {
            dashboardBack.value?.setState(MyDialog.Companion.State.DISABLE)
        }
    }


    fun onLogoutFromDashboard() {
        viewModelScope.launch {
            dashBoardUseCases.logOutFromDashboard().collect {
                when (it.type) {
                    EmitType.Navigate -> {
                        it.value?.apply {
//                            castValueToRequiredTypes<Destination>()?.let {
//                                //  scaffoldState.drawerState.close()
//                                appNavigator.navigateTo(
//                                     it.fullRoute,
//                                    //route = Destination.MobileNo(""),
//                                    popUpToRoute = Destination.Dashboard(),
//                                    inclusive = true,
//                                    isSingleTop = true
//                                )
//                            }

                            castValueToRequiredTypes<Destination.MobileNo>()?.let { destination ->
                                appNavigator.tryNavigateTo(
                                    destination.invoke(),
                                    popUpToRoute = Destination.Dashboard(),
                                    isSingleTop = true,
                                    inclusive = true
                                )
                            }

                        }
                    }
                    else -> {}
                }
            }
        }
    }


}


