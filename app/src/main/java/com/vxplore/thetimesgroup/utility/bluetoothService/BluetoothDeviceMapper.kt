package com.vxplore.thetimesgroup.utility.bluetoothService

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.vxplore.core.domain.model.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}