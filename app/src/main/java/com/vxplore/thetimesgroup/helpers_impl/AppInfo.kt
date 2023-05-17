package com.vxplore.thetimesgroup.helpers_impl

import com.vxplore.core.helpers.Info
import com.vxplore.thetimesgroup.BuildConfig
import javax.inject.Inject


class AppInfo @Inject constructor() : Info {
    override fun getCurrentVersion(): Int {
        return BuildConfig.VERSION_CODE
    }


}