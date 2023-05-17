package com.vxplore.core.domain.useCasess

import com.vxplore.core.common.*
import com.vxplore.core.domain.repositoriess.DistributorDetailsRepository
import com.vxplore.core.domain.repositoriess.DonutChartDetailsRepository
import com.vxplore.core.domain.repositoriess.TodayPaperSoldByUserIdRepository
import com.vxplore.core.domain.repositoriess.VendorDetailsRepository
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DashboardUseCases @Inject constructor(
    private val pref: AppStore,
    private val vendorDetailsRepository: VendorDetailsRepository,
    private val distributorDetailsRepository: DistributorDetailsRepository,
    private val donutChartDetailsRepository: DonutChartDetailsRepository,
    private val todayPaperSoldByUserIdRepository: TodayPaperSoldByUserIdRepository,
    private val appStore: AppStore

) {

    fun logOutFromDashboard() = flow {
        pref.logout()
        emit(Data(type = EmitType.Navigate, Destination.MobileNo))
    }




    fun getTodayPaperSoldByUserId() = flow {
        //emit(Data(EmitType.Loading, true))
        when (val response = todayPaperSoldByUserIdRepository.todayPaperSoldByUserIdRepository(appStore.userId())) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(EmitType.PaperSold, value = paperSold))
                            emit(Data(EmitType.PaperReturn, value = paperReturn))
                            //emit(Data(EmitType.EachPaperSold, value = districts))
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


    fun getVendors() = flow {
        emit(Data(EmitType.Loading, true))
        when (val response = vendorDetailsRepository.vendorDetailsRepo(appStore.userId())) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(type = EmitType.VENDORS, value = top_vendors))
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


    fun getDonutChartData() = flow {
        emit(Data(EmitType.Loading, true))
        when (val response = donutChartDetailsRepository.donutChartDetailsRepository(appStore.userId())) {
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(type = EmitType.PAPER_CODE, value = papers))
                            emit(Data(type = EmitType.TOTAL_SOLD_PAPER, value = total_sold_papers.toString()))
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


    fun getDistributorDetails() = flow {
        emit(Data(EmitType.Loading, true))
        when (val response = distributorDetailsRepository.distributorDetailsRepo(appStore.userId())) {
        //when (val response =
            is Resource.Success -> {
                emit(Data(EmitType.Loading, false))
                response.data?.apply {
                    when (status) {
                        true -> {
                            emit(Data(type = EmitType.DISTRIBUTOR_NAME, value = distributor_data.name))
                            emit(Data(type = EmitType.DISTRIBUTOR_ID, value = distributor_data.distributor_id))
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