package com.vxplore.core.domain.model

data class AppVersionModel(
    val appVersion: AppVersion,
    val message: String,
    val status: Boolean
)

data class AppVersion(
    val isSkipable: String,
    val link: String,
    val releaseDate: String,
    val versionCode: String,
    val versionName: String,
    val versionMessage: String
)