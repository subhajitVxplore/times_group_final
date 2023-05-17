package com.vxplore.thetimesgroup.helpers_impl

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.SavedStateHandle

import com.vxplore.thetimesgroup.custom_views.UiData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class SavableMutableListState<T : Iterable<*>>(
    private val key: UiData,
    private val savedStateHandle: SavedStateHandle,
    initialData: T,
) {
    val value = savedStateHandle.getStateFlow(key = key.name, initialValue = initialData)

    fun emit(data: T) {
        savedStateHandle[key.name] = data
    }
}