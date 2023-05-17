package com.vxplore.core.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val progress: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String? = null, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(progress: Int, data: T? = null) :
        Resource<T>(data, progress = progress)
}