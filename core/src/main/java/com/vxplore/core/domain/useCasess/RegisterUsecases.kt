package com.vxplore.core.domain.useCasess

import com.vxplore.core.common.*
import com.vxplore.core.domain.repositoriess.MobileNoScreenRepository
import com.vxplore.core.domain.repositoriess.RegisterRepository
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUsecases @Inject constructor(private val registerRepository: RegisterRepository,private val appStore: AppStore) {

    fun getAllState() = flow {
       // emit(Data(EmitType.Loading, true))
        when (val response = registerRepository.getAllStateRepository()) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.States, value = states))
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

    fun getDistrictByState(state: String) = flow {
        //emit(Data(EmitType.Loading, true))
        when (val response = registerRepository.getDistrictByStateRepository(state)) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.Districts, value = districts))
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

    fun getPincodeByDistrict(district: String) = flow {
       // emit(Data(EmitType.Loading, true))
        when (val response = registerRepository.getPincodeByDistrictRepository(district)) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.Pincodes, value = pincodes))
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

    fun register(name: String,email: String,mobile: String,address: String,state: String,district: String,pincode: String) = flow {
        emit(Data(EmitType.Loading, true))
        var uid=appStore.userId()
        when (val response = registerRepository.registerRepository(appStore.userId(),name,email,mobile,address,state,district,pincode)) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            //emit(Data(EmitType.Navigate, value = Destination.Otp(mobile)))
                            emit(Data(EmitType.Navigate, value = Destination.Dashboard()))
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