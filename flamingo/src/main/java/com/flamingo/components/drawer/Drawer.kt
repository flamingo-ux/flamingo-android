package com.flamingo.components.drawer

import android.view.Gravity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.modal.ButtonParams
import com.flamingo.components.modal.Modal
import com.flamingo.components.modal.ModalBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *  Displays custom [content] as a fullscreen dialog with optional header and action buttons,
 *  with horizontal open/close animation.
 *  Width can be limited up to 50% of max width. [content] will scroll vertically if the limit is
 *  exceeded.
 *
 *  @param isVisible handles visibility of the [Drawer] dialog
 *  @param title optional header title
 *  @param hasCloseButton optional button inside header, that invokes [onDismissRequest]
 *  @param onDismissRequest callback that is called when user tries to dismiss the dialog.
 *  It is __needed__ to include [isVisible] change inside this callback to properly close [Drawer]
 *  @param isLTRAnimation determines sliding animation direction (LTR or RTL)
 *  @param widthFraction the fraction of the maximum width to use, between 0.5 and 1, inclusive
 *  @param properties default [Dialog] properties. __NOTE__ usePlatformDefaultWidth is ignored,
 *  because it is defaulted to false to avoid width issues.
 *  @param primaryButtonParams optional action button
 *  @param secondaryButtonParams secondary action button that is always present
 *  @param content content of the [Modal] dialog
 *
 * @sample com.flamingo.playground.preview.DrawerPreview
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.DrawerPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=28681%3A200341&t=WaNR9MLvhraHPQW8-0",
    specification = "https://confluence.companyname.ru/x/j4cjKQE",
    demo = ["com.flamingo.playground.components.drawer.DrawerStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Drawer(
    isVisible: Boolean,
    title: String? = null,
    hasCloseButton: Boolean = true,
    onDismissRequest: () -> Unit,
    isLTRAnimation: Boolean = true,
    widthFraction: Float = 1f,
    properties: DialogProperties = DialogProperties(),
    primaryButtonParams: ButtonParams?,
    secondaryButtonParams: ButtonParams,
    content: @Composable () -> Unit
) {
    require(widthFraction in 0.5f..1f) {
        "widthFraction should be in the range from 0.5 to 1!"
    }
    if (isVisible) {
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        val animateContentBackTrigger = remember { mutableStateOf(false) }

        LaunchedEffect(key1 = Unit) {
            launch {
                animateContentBackTrigger.value = true
            }
        }
        Dialog(
            onDismissRequest = {
                coroutineScope.launch {
                    startDismissWithExitAnimation(animateContentBackTrigger, onDismissRequest)
                }
            }, properties = DialogProperties(
                dismissOnClickOutside = properties.dismissOnClickOutside,
                dismissOnBackPress = properties.dismissOnBackPress,
                securePolicy = properties.securePolicy,
                usePlatformDefaultWidth = false
            )
        ) {
            val dialogWindowProvider = LocalView.current.parent as? DialogWindowProvider
            dialogWindowProvider?.window?.setGravity(
                if (isLTRAnimation) Gravity.START else Gravity.END
            )

            Box(
                Modifier
                    .fillMaxWidth(widthFraction)
                    .fillMaxHeight()
            ) {
                AnimatedVisibility(visible = animateContentBackTrigger.value,
                    enter = slideInHorizontally(animationSpec) { if (isLTRAnimation) -it else it },
                    exit = slideOutHorizontally(animationSpec) { if (isLTRAnimation) -it else it }
                ) {
                    ModalBase(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Flamingo.colors.background),
                        title = title,
                        hasCloseButton = hasCloseButton,
                        onDismissRequest = {
                            coroutineScope.launch {
                                startDismissWithExitAnimation(
                                    animateContentBackTrigger,
                                    onDismissRequest
                                )
                            }
                        },
                        primaryButtonParams = primaryButtonParams,
                        secondaryButtonParams = secondaryButtonParams,
                        content = content
                    )
                }
            }
        }
    }
}

private suspend fun startDismissWithExitAnimation(
    animateContentBackTrigger: MutableState<Boolean>,
    onDismissRequest: () -> Unit
) {
    animateContentBackTrigger.value = false
    delay(ANIMATION_DURATION)
    onDismissRequest()
}

private const val ANIMATION_DURATION = 300L
private val animationSpec = tween<IntOffset>(durationMillis = ANIMATION_DURATION.toInt())
