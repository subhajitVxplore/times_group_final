package com.vxplore.core.domain.useCasess

import android.util.Log
import com.vxplore.core.common.Action
import com.vxplore.core.common.Resource
import com.vxplore.core.domain.model.Command
import com.vxplore.core.domain.repositoriess.SearchVendorRepository
import com.vxplore.core.helpers.AppStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchVendorUseCases @Inject constructor(private  val searchVendorRepository: SearchVendorRepository,private val pref: AppStore) {



    fun generateVendorsSuggestions(query: String): Flow<Command> = flow {

        val userID = pref.userId()
        Log.d("TESTING", "USER_ID ${pref.userId()}")
        val response = searchVendorRepository.searchVendorRepository(userID ?: "", query)

        when (response) {
            is Resource.Success -> {
                response.data?.apply {
                    when (status) {
                        true -> {
                            if(vendors != null) {
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
                            emit(Command(Action.BACKEND_ERROR, message+userID))
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

}