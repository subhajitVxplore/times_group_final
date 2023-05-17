package com.vxplore.thetimesgroup.mainController

import android.app.Application
import com.vxplore.thetimesgroup.utility.Metar
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltControllerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Metar.initialize(this)
        app = this
    }

    companion object {
        var app: HiltControllerApp? = null
    }
}