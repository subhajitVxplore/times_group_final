package com.vxplore.thetimesgroup.viewModels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caysn.autoreplyprint.AutoReplyPrint
import com.example.core.utils.AppNavigator
import com.sun.jna.Pointer
import com.vxplore.core.common.Action
import com.vxplore.core.common.Destination
import com.vxplore.core.common.EmitType
import com.vxplore.core.domain.model.*
import com.vxplore.thetimesgroup.utility.bluetoothService.BluetoothController
import com.vxplore.core.domain.useCasess.BillingScreenUseCases
import com.vxplore.core.helpers.AppStore
import com.vxplore.thetimesgroup.custom_views.UiData
import com.vxplore.thetimesgroup.extensions.castListToRequiredTypes
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import com.vxplore.thetimesgroup.helpers_impl.SavableMutableState
import com.vxplore.thetimesgroup.utility.bluetoothService.BluetoothUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BillingScreenViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val billingScreenUseCases: BillingScreenUseCases,
    private val bluetoothController: BluetoothController,
    private val pref: AppStore,
    savedStateHandle: SavedStateHandle
) : ViewModel(), AutoReplyPrint.CP_OnPortOpenedEvent_Callback {//BillingScreenViewModel

    private var _printerPointer: Pointer? = null
    var pPointer = _printerPointer


    var pairedDevicess = listOf<BluetoothDevice>()
    var scannedDevicess = mutableListOf<BluetoothDevice>()
    private val _state = MutableStateFlow(BluetoothUiState())
    var btState = mutableStateOf(1)

    //val mainActivity:MainActivity = TODO()
    //val mainActivity = mutableStateOf<MainActivity>()
    //var expand = mutableStateOf(false)  // Expand State
    var expandReturn = mutableStateOf(false)  // Expand State
    var expandCoupon = mutableStateOf(false)  // Expand State
    var stroke = mutableStateOf(1)

    var vendorPhone = mutableStateOf("")
    var cashPayment = mutableStateOf(0)
    var cashPaymentText = mutableStateOf("")
    var previousDue = mutableStateOf(0f)
    var currentDue = mutableStateOf(0f)
    var balanceAmount = mutableStateOf(0f)
    var isAddedBillData = mutableStateOf(false)
    var pdfData = mutableStateOf("")

    var generateBillButtonText: String = "Generate Bill"
    // var takenPapers = MutableList<Pair<Int, Int>>(getPaperPrice().size) { Pair(0, 0) }

    private val _paperss = MutableStateFlow(emptyList<Paper>())
    val paperss = _paperss.asStateFlow()
    private val _couponss = MutableStateFlow(emptyList<Coupon>())
    val couponss = _couponss.asStateFlow()

    val takenPapers = MutableStateFlow(mutableListOf<SendTodayPapers>())
    val takenPapersJason = MutableStateFlow(mutableListOf<SendTodayPapers>())
    val returnPapers = MutableStateFlow(mutableListOf<SendReturnPapers>())
    val returnPapersJason = MutableStateFlow(mutableListOf<SendReturnPapers>())
    var takenMinusreturnPaperTotal = mutableStateOf(0f)
    val coupons = MutableStateFlow(mutableListOf<SendCoupons>())
    val couponsJason = MutableStateFlow(mutableListOf<SendCoupons>())
    var cashMinusCouponTotal = mutableStateOf(0f)

    private val _suggestionsss: MutableStateFlow<List<SearchVendorModel>> =
        MutableStateFlow(emptyList())
    private val _suggestions = MutableStateFlow(emptyList<SearchVendor>())
    val suggestion = _suggestions.asStateFlow()
    var suggestionListVisibility by mutableStateOf(false)
    private var suggestionsBackup: List<SearchVendorModel> = emptyList()
    var searchVendorQuery by mutableStateOf("")

    val toastError = mutableStateOf("")

    var vendorId by mutableStateOf("")

    // lateinit var takenPapersKey: List<SendTodayPapers>
    //val takenPapersKey = MutableStateFlow(mutableListOf<SendTodayPapers>())
    var takenPapersKey: List<SendTodayPapers> = emptyList()
    var returnPapersKey: List<SendReturnPapers> = emptyList()

    init {
        currentDue.value = previousDue.value
        //Log.d("pairedDevicess", "pairedDevicess: ${bluetoothController.pairedDevices.value}")
       cashPayment.value
    }


    val takenPapersTotal = mutableStateOf<Float>(0f)
    val returnsTotal = mutableStateOf<Float>(0f)
    val couponsTotal = mutableStateOf<Float>(0f)


    fun setPrinterPointer(p: Pointer) {
        _printerPointer = p
        pPointer = p
    }

    fun calculateTakenPapersPrice(value1: Float, value2: Float, index: Int) {
        takenPapers.update { values ->
            values[index].value = value1 * value2
            takenPapersTotal.value = values.sumOf {
                it.value.toDouble()
            }.toFloat()
            values
        }
        takenPapersJason.update { values ->
            values[index].value = value2
            values
        }
    }

    fun calculateReturnPapersPrice(value1: Float, value2: Float, index: Int) {
        returnPapers.update { values ->
//            values[index] = value1 * value2
//            returnsTotal.value = values.sum()
            values[index].value = value1 * value2
            returnsTotal.value = values.sumOf { it.value.toDouble() }.toFloat()
            values
        }
        returnPapersJason.update { values ->
            values[index].value = value2
            values
        }


    }

    fun calculateCouponPrice(value1: Int, value2: Int, index: Int) {
        //couponsJason.value=coupons.value
        coupons.update { values ->
            values[index] = values[index].copy(value = value1 * value2)
            couponsTotal.value = values.sumOf { it.value.toDouble() }.toFloat()
            values
        }
        couponsJason.update { values ->
            values[index] = values[index].copy(value = value2)
            values
        }


    }


    fun onBillingToAddVendor() {
        appNavigator.tryNavigateTo(
            route = Destination.AddVendor(),
            // popUpToRoute = Destination.Dashboard(),
            //isSingleTop = true,
            //inclusive = true
        )
    }

    fun onBillingToBillPreview() {
        appNavigator.tryNavigateTo(
            route = Destination.BillPreview(),
            // popUpToRoute = Destination.Billing(),
            isSingleTop = true,
            inclusive = true
        )
    }

    val circleLoading = SavableMutableState(
        key = UiData.PaperApiLoading,
        savedStateHandle = savedStateHandle,
        initialData = false
    )

    fun getPapersByVendorId(vendor_id: String) {
        billingScreenUseCases.getPapersByVendorId(vendor_id)//original
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Boolean>()?.let {
                                circleLoading.setValue(it)
                            }
                        }
                    }
                    EmitType.PAPERS -> {
                        it.value?.castListToRequiredTypes<Paper>()?.let { papers ->
                            _paperss.update { papers }

                            takenPapers.update {
                                MutableList(papers.size) {
                                    SendTodayPapers(key = papers[it].key, value = 0f)
                                }
                            }
                            takenPapersJason.update {
                                MutableList(papers.size) {
                                    SendTodayPapers(key = papers[it].key, value = 0f)
                                }
                            }

                            returnPapers.update {
                                //MutableList(papers.size) { 0 }
                                MutableList(papers.size) {
                                    SendReturnPapers(key = papers[it].key, value = 0f)
                                }
                            }
                            returnPapersJason.update {
                                MutableList(papers.size) {
                                    SendReturnPapers(key = papers[it].key, value = 0f)
                                }
                            }
                        }
                    }
                    EmitType.COUPONS -> {
                        it.value?.castListToRequiredTypes<Coupon>()?.let { coupon ->
                            _couponss.update { coupon }
//                            coupons.update {
//                                MutableList(coupon.size) { idx ->
//                                    Coupon(key = coupon[idx].key, value = 0)
//                                }
//                            }
//                            couponsJason.update {
//                                MutableList(coupon.size) { idx ->
//                                    Coupon(key = coupon[idx].key, value = 0)
//                                }
//                            }


                            coupons.update {
                                //MutableList(papers.size) { 0 }
                                MutableList(coupon.size) {
                                    SendCoupons(key = coupon[it].key, value = 0)
                                }
                            }
                            couponsJason.update {
                                MutableList(coupon.size) {
                                    SendCoupons(key = coupon[it].key, value = 0)
                                }
                            }

                        }
                    }

                    EmitType.DUE -> {
                        it.value?.castValueToRequiredTypes<Float>()?.let {
                            previousDue.value = it
                        }
                    }
                    EmitType.PHONE -> {
                        it.value?.castValueToRequiredTypes<String>()?.let {
                            vendorPhone.value = it
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
////////////////////////////////////////////////////////////////////////////

    fun clearVendorsQuery() {
        // getPapersByVendorId(" ")
        _paperss.update { emptyList() }
        _couponss.update { emptyList() }
        searchVendorQuery = ""
        loadingBill.value = true
        takenPapersTotal.value=0f
        returnsTotal.value=0f
        takenMinusreturnPaperTotal.value = 0f
        cashPayment.value = 0
        couponsTotal.value = 0f
        cashMinusCouponTotal.value = 0f
        currentDue.value = 0f
        balanceAmount.value=0f
        pdfData.value = ""
        expandReturn.value = false
        expandCoupon.value = false
        suggestionListVisibility = false
        viewModelScope.launch {
            suggestionsBackup.apply {
                _suggestionsss.emit(this)
            }
        }
    }

    fun updateVendorsQuery(query: String) {
        searchVendorQuery = query
        getVendorsSuggestions(query = searchVendorQuery)
    }

    private fun getVendorsSuggestions(query: String) {
        billingScreenUseCases.generateVendorsSuggestions(query = query).onEach {
            when (it.actionType) {
                Action.SUGGESTIONS -> {
                    it.targetType?.castListToRequiredTypes<SearchVendor>()?.let {
                        _suggestions.emit(it)
                    }
                }
                Action.NO_SUGGESTIONS -> {
                    it.targetType?.let {
                        val bool = it as? Boolean
                        bool?.apply {
                            suggestionListVisibility = this
                        }
                    }
                }
                Action.BACKEND_ERROR -> {
                    it.targetType?.castValueToRequiredTypes<String>()?.let {
                        toastError.value = it
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    var loadingBill = mutableStateOf(true)

    fun generateBillByJson() {

        val rawJsonData = GenerateBillDataRequestModel(
            vendor_id = vendorId,
            calculated_price = takenMinusreturnPaperTotal.value,
            payment_by_cash = cashPayment.value,
            due_amount = previousDue.value,
            coupons = couponsJason.value.filter { it.value != 0 },//as same class name in both two model classes(PapersByVendorIdModel & GenerateBillDataRequestModel)
            today_papers = takenPapersJason.value.filter { it.value != 0f },
            return_papers = returnPapersJason.value.filter { it.value != 0f }
        )



        billingScreenUseCases.generateBillByJson(rawJsonData)
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.Loading -> {
                        it.value?.castValueToRequiredTypes<Boolean>()?.let {
                            loadingBill.value = it
                        }
                    }
                    EmitType.IS_ADDED -> {
                        it.value?.castValueToRequiredTypes<Boolean>()?.let {
                            isAddedBillData.value = it
                        }
                    }
                    EmitType.PDF_URL -> {
                        it.value?.castValueToRequiredTypes<String>()?.let {
                            pdfData.value = it
//                            pref.storePdfUrl(it)
//                            pdfUrl.value=pref.fetchPdfUrl()
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

    val scannedBLdevices = mutableStateListOf<BluetoothDevice>()

    var printerFound by mutableStateOf<Printer?>(null)

    var printerConnectedNotify by mutableStateOf("")


    fun checkIfDeviceFound() {
        viewModelScope.launch {
            val printerID = pref.printer()
            if (printerID != null) {
                printerFound = Printer(
                    address = printerID
                )
            } else {
                bluetoothController.startDiscovery()
                bluetoothController.scannedDevices.onEach { devices ->
                    if (!scannedBLdevices.containsAll(devices)) {
                        scannedBLdevices.clear()
                        scannedBLdevices.addAll(devices.distinctBy { it.name })
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    override fun CP_OnPortOpenedEvent(p0: Pointer?, p1: String?, p2: Pointer?) {
        printerConnectedNotify = "Printer Connection ${p0.toString()} $p1 ${p2.toString()}"
        generateBillByJson()
    }

    fun onClickDevice(bluetoothDevice: BluetoothDevice) {
        scannedBLdevices.clear()
        bluetoothController.stopDiscovery()
        viewModelScope.launch { pref.savePrinter(bluetoothDevice.address) }
        printerFound =
            Printer(address = bluetoothDevice.address, name = bluetoothDevice.name ?: "Printer")
        Log.d("BLE DEVICE", "onClickDevice: ${bluetoothDevice.address}")
    }
}


data class Printer(val address: String, val name: String = "Billing Printer")