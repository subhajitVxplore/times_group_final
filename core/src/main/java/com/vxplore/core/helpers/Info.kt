package com.vxplore.core.helpers

import android.content.ContentResolver

interface Info {
    fun getCurrentVersion(): Int

    //fun getDeviceId(resolver: ContentResolver): String
}