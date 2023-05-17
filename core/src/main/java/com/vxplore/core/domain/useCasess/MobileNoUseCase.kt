package com.vxplore.core.domain.useCasess

import com.vxplore.core.common.*
import com.vxplore.core.domain.repositoriess.MobileNoScreenRepository
import com.vxplore.core.domain.repositoriess.OtpRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MobileNoUseCase @Inject constructor(private val mobileNoScreenRepository: MobileNoScreenRepository) {

    fun sendOtp(number: String) = flow {
        emit(Data(EmitType.Loading, true))
        when (val response = mobileNoScreenRepository.sendOtpRepository(number)) {

            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.Navigate, value = Destination.Otp(number)))
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