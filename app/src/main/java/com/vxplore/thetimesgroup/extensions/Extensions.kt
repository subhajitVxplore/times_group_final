package com.vxplore.thetimesgroup.extensions

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import com.vxplore.thetimesgroup.helpers_impl.SavableMutableState


@Composable
fun Int.resourceString(): String {
    return stringResource(id = this)
}

//fun Int.string(): String {
//    return FarmologyDeliveryApp.appInstance.baseContext.resources.getString(this)
//}

@Composable
fun Int.dimen(): Dp {
    return dimensionResource(id = this)
}

@OptIn(ExperimentalUnitApi::class, ExperimentalUnitApi::class)
@Composable
fun Int.toSp(): TextUnit {
    return TextUnit(dimensionResource(id = this@toSp).value, TextUnitType.Sp)
}


fun String.shortToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}


fun String.longToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}


fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

//
//fun <T> handleExceptions(: HttpClient, response: T): Resource<T> {
//    return try {
//        Resource.Success(data = response)
//    } catch (ex: RedirectResponseException) {
//        Resource.Error(ex.message ?: "")
//    } catch (ex: ClientRequestException) {
//        Resource.Error(ex.message ?: "")
//    } catch (ex: ServerResponseException) {
//        Resource.Error(ex.message ?: "")
//    } catch (ex: Exception) {
//        Resource.Error(ex.message ?: "")
//    }
//}


fun String.asColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}



fun <T> Any.castListToRequiredTypes(): List<T>? {
    val items = mutableListOf<T>()

    if (this !is List<*>) return null

    forEach { item -> item?.let { items.add(it as T) } }

    return items.toList()
}

inline fun <reified T1, reified T2> Any.castMapToRequiredTypes(): Map<T1, T2>? {

    val data = mutableMapOf<T1, T2>()

    if (this !is Map<*, *>) return null

    forEach { (k, v) -> data[k as T1] = v as T2 }

    return data.toMap()
}


inline fun <reified T> Any.castValueToRequiredTypes(): T? {
    return this as? T
}

inline fun <reified K, reified V> Any.castPairToRequiredType(): Pair<K, V>? {

    if (this !is Pair<*, *>) return null

    return (this.first as K) to (this.second as V)
}


@Composable
fun <T> MutableState<T>.ComposeLaunchEffect(
    intentionalCode: suspend (T) -> Unit,
    clearance: () -> T,
) {
    LaunchedEffect(key1 = value) {
        value?.let {
            intentionalCode(it)
            value = clearance()
        }
    }
}

@UiComposable
@Composable
fun <T> SavableMutableState<T>.OnEffect(
    intentionalCode: suspend (T) -> Unit,
    clearance: (() -> T)? = null,
) {
    LaunchedEffect(key1 = value) {
        value?.let {
            intentionalCode(it)
            if(clearance != null) {
                reset(clearance())
            }
        }
    }
}

val screenHeight: Dp
    @Composable
    @ReadOnlyComposable
    get() = LocalConfiguration.current.screenHeightDp.dp

val screenWidth: Dp
    @Composable
    @ReadOnlyComposable
    get() = LocalConfiguration.current.screenWidthDp.dp


fun <T> animationSpec() = tween<T>(
    durationMillis = 400,
    easing = LinearOutSlowInEasing
)

@OptIn(ExperimentalAnimationApi::class)
fun upToBottom() = slideInVertically(
    initialOffsetY = {-it},
    animationSpec = animationSpec()
) + fadeIn(animationSpec = animationSpec()) with slideOutVertically(
    targetOffsetY = {it},
    animationSpec = animationSpec()
) + fadeOut(animationSpec())


@OptIn(ExperimentalAnimationApi::class)
fun bottomToUp() = slideInVertically(
    initialOffsetY = {it},
    animationSpec = animationSpec()
) + fadeIn(animationSpec()) with slideOutVertically(
    targetOffsetY = {-it},
    animationSpec = animationSpec()
) + fadeOut(animationSpec = animationSpec())