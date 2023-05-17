package com.vxplore.core.domain.useCasess

import android.util.Log
import com.vxplore.core.common.*
import com.vxplore.core.domain.model.Command
import com.vxplore.core.domain.repositoriess.AddVendorRepository
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddVendorUseCases @Inject constructor(
    private val addVendorRepository: AddVendorRepository,
    private val appStore: AppStore
) {

    fun addVendor(name: String, mobile: String, email: String, pincodes: String) = flow {
        //emit(Data(EmitType.Loading, true))

        when (val response = addVendorRepository.addVendorRepository(
            appStore.userId(),
            name,
            mobile,
            email,
            pincodes
        )) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.Navigate, value = Destination.AddVendorSuccess(name)))
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


    fun getPincodesByDistributorId() = flow {
        // emit(Data(EmitType.Loading, true))
        when (val response =
            addVendorRepository.pincodesByDistributorIdRepository(appStore.userId())) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(
                                Data(
                                    type = EmitType.Pincodes,
                                    pincodes
                                )
                            )
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