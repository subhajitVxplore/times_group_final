package com.vxplore.thetimesgroup.utility.bluetoothService

import com.vxplore.core.domain.model.BluetoothDevice


data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
)
