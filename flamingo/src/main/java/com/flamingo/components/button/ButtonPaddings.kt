package com.flamingo.components.button

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.utils.exhaustive

/**
 * [MutableState]s are used to extract "padding logic" from [Button] composable function body
 * without creating a data holder class instance every time the function returns.
 */
internal class ButtonPaddings {
    var textStart by mutableStateOf(0.dp)
    var textEnd by mutableStateOf(0.dp)
    var iconStart by mutableStateOf(0.dp)
    var iconEnd by mutableStateOf(0.dp)

    @Suppress("NestedBlockDepth", "MagicNumber", "ComplexMethod", "LongMethod")
    fun calculatePaddings(
        variant: ButtonVariant,
        sizeLarge: Boolean,
        isIconPresent: Boolean,
        iconPosition: ButtonIconPosition,
        loading: Boolean,
    ) {
        /** Properties are [Int]s and not [Dp]s because .[dp] calls will clutter code */
        var textStartDp = 0
        var textEndDp = 0
        var iconStartDp = 0
        var iconEndDp = 0

        when (variant) {
            ButtonVariant.CONTAINED -> {
                textStartDp = if (sizeLarge) 20 else 16
                textEndDp = if (sizeLarge) 20 else 16

                if (isIconPresent || loading) when {
                    iconPosition == ButtonIconPosition.START || loading -> {
                        textStartDp = 8
                        iconStartDp = if (sizeLarge) 16 else 12
                    }
                    iconPosition == ButtonIconPosition.END -> {
                        textEndDp = 8
                        iconEndDp = if (sizeLarge) 16 else 12
                    }
                    else -> error("Unknown icon position: $iconPosition")
                } else Unit
            }
            ButtonVariant.TEXT -> {
                textStartDp = if (sizeLarge) 16 else 8
                textEndDp = if (sizeLarge) 16 else 8

                if (isIconPresent || loading) when {
                    iconPosition == ButtonIconPosition.START || loading -> {
                        textStartDp = 8
                        iconStartDp = if (sizeLarge) 12 else 8
                    }
                    iconPosition == ButtonIconPosition.END -> {
                        textEndDp = 8
                        iconEndDp = if (sizeLarge) 12 else 8
                    }
                    else -> error("Unknown icon position: $iconPosition")
                } else Unit
            }
        }.exhaustive

        textStart = textStartDp.dp
        textEnd = textEndDp.dp
        iconStart = iconStartDp.dp
        iconEnd = iconEndDp.dp
    }
}
