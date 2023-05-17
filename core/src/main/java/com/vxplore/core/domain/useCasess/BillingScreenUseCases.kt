package com.vxplore.core.domain.useCasess

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.vxplore.core.common.*
import com.vxplore.core.domain.model.Command
import com.vxplore.core.domain.model.GenerateBillDataRequestModel
import com.vxplore.core.domain.repositoriess.GenerateBillRepository
import com.vxplore.core.domain.repositoriess.PapersByVendorIdRepository
import com.vxplore.core.domain.repositoriess.SearchVendorRepository
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BillingScreenUseCases @Inject constructor(
    private val searchVendorRepository: SearchVendorRepository,
    private val papersByVendorIdRepository: PapersByVendorIdRepository,
    private val generateBillRepository: GenerateBillRepository,
    private val pref: AppStore
) {
    fun generateVendorsSuggestions(query: String): Flow<Command> = flow {

        val userID = pref.userId()
        Log.d("TESTING", "USER_ID ${pref.userId()}")
        val response = searchVendorRepository.searchVendorRepository(userID ?: "", query)

        when (response) {
            is Resource.Success -> {
                response.data?.apply {
                    when (status) {
                        true -> {
                            if (vendors != null) {
                                if (vendors.isNotEmpty()) {
                                    //emit(Command(Action.NO_SUGGESTIONS, true)) //optional
                                    emit(Command(Action.SUGGESTIONS, vendors))
                                } else {
                                    emit(Command(Action.NO_SUGGESTIONS, false))
                                }
                            } else {
                                emit(Command(Action.BACKEND_ERROR, message))
                            }
                        }
                        else -> {
                           // emit(Command(Action.BACKEND_ERROR, message + userID))
                        }
                    }
                }
            }
            is Resource.Error -> {
//                emit(Command(Action.NETWORK_ERROR,
//                    textProvider.getText(TextIdentity.SOMETHING_WRONG)))
            }
            else -> {}
        }
    }


    fun getPapersByVendorId(vendor_id: String) = flow {
        emit(Data(EmitType.Loading, true))
        when (val response = papersByVendorIdRepository.papersByVendorIdRepository(vendor_id)) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.PHONE, value = phoneNumber))
                            emit(Data(EmitType.PAPERS, value = papers))
                            emit(Data(EmitType.COUPONS, value = coupons))
                            emit(Data(EmitType.DUE, value = dueAmount))
                            emit(Data(type = EmitType.INFORM, value = message))
                        }
                        else -> {
                            emit(Data(type = EmitType.BackendError, value = message))
                        }
                    }
                }
            }
            is Resource.Error -> {
                handleFailedResponse(
                    response = response,
                    message = response.message,
                    emitType = EmitType.NetworkError
                )
            }
            else -> {

            }

        }
    }



    fun generateBillByJson(rawJson: GenerateBillDataRequestModel) = flow {

        emit(Data(EmitType.Loading, true))
        when (val response = generateBillRepository.generateBillRepository(rawJson)) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.IS_ADDED, value = isAdded))
                            emit(Data(EmitType.PDF_URL, value = pdfUrl))
                            emit(Data(type = EmitType.INFORM, value = message))
                        }
                        else -> {
                            emit(Data(type = EmitType.BackendError, value = message))
                        }
                    }
                }
            }
            is Resource.Error -> {
                handleFailedResponse(
                    response = response,
                    message = response.message,
                    emitType = EmitType.NetworkError
                )
            }
            else -> {

            }

        }
    }


}