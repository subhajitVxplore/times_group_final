package com.vxplore.thetimesgroup.viewModels;

import android.app.Dialog
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.AppNavigator
import com.vxplore.core.common.Destination
import com.vxplore.core.common.DialogData
import com.vxplore.core.common.EmitType
import com.vxplore.core.common.IntroStatus
import com.vxplore.core.domain.model.AppVersion
import com.vxplore.core.domain.model.BaseUrlModel
import com.vxplore.core.domain.model.Vendor
import com.vxplore.core.domain.useCasess.SplashUseCases
import com.vxplore.core.helpers.AppStore
import com.vxplore.thetimesgroup.custom_views.UiData
import com.vxplore.thetimesgroup.extensions.MyDialog
import com.vxplore.thetimesgroup.extensions.castListToRequiredTypes
import com.vxplore.thetimesgroup.extensions.castValueToRequiredTypes
import com.vxplore.thetimesgroup.helpers_impl.SavableMutableState
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val splashUseCases: SplashUseCases,
    private val pref: AppStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        getBaseUrll()
    }

    val versionUpdateDialog = mutableStateOf<MyDialog?>(null)

    private suspend fun checkIntroStatus() {
        splashUseCases.checkIntroStatus()
            .flowOn(Dispatchers.Default)
            .collect { dataEntry ->
                when (dataEntry.type) {
                    EmitType.IntroStatus -> {
                        dataEntry.value?.apply {
                            castValueToRequiredTypes<IntroStatus>()?.let {
                                // splashBtnStatus.setValue(it)
                            }
                        }
                    }
                    else -> {}
                }
            }
    }

    fun onSplashRun(destination: Destination.MobileNo = Destination.MobileNo) {
        appNavigator.tryNavigateTo(
            destination("0"),
            popUpToRoute = Destination.Splash(),
            isSingleTop = true,
            inclusive = true
        )
    }

    private suspend fun checkAppVersion() {
        splashUseCases.checkAppVersion()
            .flowOn(Dispatchers.IO)
            .collect {
                when (it.type) {
                    EmitType.BackendError -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let {

                            }
                        }
                    }
                    EmitType.AppVersion -> {
                        it.value?.apply {
                            castValueToRequiredTypes<AppVersion>()?.let { appVersion ->
                                versionUpdateDialog.value = MyDialog(
                                    data = DialogData(
                                        title = "Version Update",
                                        message = appVersion.versionMessage,
                                        positive = "Update Now",
                                        negative = "Update Later",
                                        data = appVersion
                                    )
                                )
                                handleDialogEvents()
                            }
                        }
                    }
                    EmitType.Navigate -> {
                        it.value?.apply {

                            castValueToRequiredTypes<Destination.NoArgumentsDestination>()?.let { destination ->
                                appNavigator.tryNavigateTo(
                                    destination(),
                                    popUpToRoute = Destination.Splash(),
                                    isSingleTop = true,
                                    inclusive = true
                                )
                            }

                            castValueToRequiredTypes<Destination.MobileNo>()?.let { destination ->
                                appNavigator.tryNavigateTo(
                                    destination.invoke(),
                                    popUpToRoute = Destination.Splash(),
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
            }
    }

    val versionUpdateLink = SavableMutableState<String?>(
        key = UiData.AppStoreLink,
        savedStateHandle = savedStateHandle,
        initialData = null
    )

    private fun handleDialogEvents() {
        versionUpdateDialog.value?.onConfirm = {
            it?.castValueToRequiredTypes<AppVersion>()?.apply {
                versionUpdateLink.setValue(link)
            }
        }
        versionUpdateDialog.value?.onDismiss = {
            versionUpdateDialog.value?.setState(MyDialog.Companion.State.DISABLE)
            splashUseCases.navigateToAppropiateScreen().onEach {
                when (it.type) {
                    EmitType.Navigate -> {
                        it.value?.apply {
                            castValueToRequiredTypes<Destination.NoArgumentsDestination>()?.let {
                                appNavigator.navigateTo(
                                    it(),
                                    popUpToRoute = Destination.Splash(),
                                    inclusive = true
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
        }
    }


    fun getBaseUrll() {
        splashUseCases.getBaseUrll()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it.type) {
                    EmitType.BaseUrl -> {
                        it.value?.apply {
                            castValueToRequiredTypes<String>()?.let {
                                pref.storeBaseUrl(it)
                                checkIntroStatus()
                                checkAppVersion()
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


