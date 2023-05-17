package com.vxplore.core.domain.useCasess


import com.vxplore.core.helpers.AppStore
import com.vxplore.core.helpers.Info
import com.vxplore.core.common.*
import com.vxplore.core.domain.repositoriess.BaseUrlRepository
import com.vxplore.core.domain.repositoriess.SplashRepository
import com.vxplore.core.domain.repositoriess.VendorDetailsRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashUseCases @Inject constructor(
    private val splashRepository: SplashRepository,
    private val appInfo: Info,
    private val appStore: AppStore
) {

    fun checkIntroStatus() = flow {
        if (!appStore.isIntroDone()) {
            appStore.intro(true)
            emit(Data(type = EmitType.IntroStatus, IntroStatus.NOT_DONE))
        } else {
            emit(Data(type = EmitType.IntroStatus, IntroStatus.DONE))
        }
    }

    fun checkAppVersion() = flow {
        val currentVersion = appInfo.getCurrentVersion()
        when (val response = splashRepository.appVersion(currentVersion)) {
            is Resource.Success -> {
                response.data?.apply {
                    when (status) {
                        true -> {
                            if (currentVersion < appVersion.versionCode.toInt()) {
                                emit(Data(type = EmitType.AppVersion, value = appVersion))
                            } else {
                              //  if (appStore.fetchRegistrationStatus() == "REGISTERED"){
                                    if(appStore.isLoggedIn()) {
                                        emit(Data(type = EmitType.Navigate,value = Destination.Dashboard))
                                    } else {
                                        emit(Data(type = EmitType.Navigate,value = Destination.MobileNo))
                                    }
                               //}else{emit(Data(type = EmitType.Navigate,value = Destination.Register))}
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


    fun navigateToAppropiateScreen() = flow {
        if(appStore.isLoggedIn()) {
            emit(Data(type = EmitType.Navigate, value = Destination.Dashboard))
        } else {
            emit(Data(type = EmitType.Navigate, value = Destination.MobileNo))
        }
    }

    fun getBaseUrll() = flow{
        when (val response = splashRepository.baseUrl()) {
            is Resource.Success -> {
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(type = EmitType.BaseUrl, value = base_url))
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