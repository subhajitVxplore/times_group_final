package com.vxplore.core.domain.useCasess

import android.content.Context
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.*
import com.vxplore.core.domain.repositoriess.OtpRepository
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OtpUseCases @Inject constructor(
    private val otpRepository: OtpRepository,
    private val pref: AppStore
    ) {

    fun verifyOtp(number: String?,otp: String) = flow {
        emit(Data(EmitType.Loading, true))
        when (val response = otpRepository.verifyOtpRepository(number.toString(),otp)) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            if (isMatched) {
                                pref.login(userId)
                                pref.storeRegistrationStatus(userStatus)
                                  if (pref.fetchRegistrationStatus() == "REGISTERED"){
                                // if (userStatus.contains("REGISTERED")) {
                                   // pref.login(userId)
                                    emit(Data(EmitType.Navigate, value = Destination.Dashboard()))
                                } else {
                                    emit(Data(EmitType.Navigate,value = Destination.Register(number.toString())))
                                }
                                //}else{emit(Data(type = EmitType.Navigate,value = Destination.Register))}
                            } else {
                                emit(Data(EmitType.Navigate,value = Destination.MobileNo(number ?: "")))
                                emit(Data(type = EmitType.ERROR, value = message))
                            }
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