package com.vxplore.core.common

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import kotlinx.coroutines.flow.FlowCollector


suspend inline fun <reified R> FlowCollector<Data>.handleFailedResponse(
    response: Resource<R>,
    message: String?,
    emitType: EmitType
) {
    when (message != null) {
        true -> {
            emit(Data(emitType, message))
        }
        else -> {
            emit(Data(emitType, response.message))
        }
    }
}



//
//fun <T> animationSpec() = tween<T>(
//    durationMillis = 400,
//    easing = LinearOutSlowInEasing
//)
//@OptIn(ExperimentalAnimationApi::class)
//fun upToBottom() = slideInVertically(
//    initialOffsetY = {-it},
//    animationSpec = animationSpec()
//) + fadeIn(animationSpec = animationSpec()) with slideOutVertically(
//    targetOffsetY = {it},
//    animationSpec = animationSpec()
//) + fadeOut(animationSpec())
//
//
//@OptIn(ExperimentalAnimationApi::class)
//fun bottomToUp() = slideInVertically(
//    initialOffsetY = {it},
//    animationSpec = animationSpec()
//) + fadeIn(animationSpec()) with slideOutVertically(
//    targetOffsetY = {-it},
//    animationSpec = animationSpec()
//) + fadeOut(animationSpec = animationSpec())