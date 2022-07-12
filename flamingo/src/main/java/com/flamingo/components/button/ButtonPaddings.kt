package com.flamingo.components.button

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
        isIconPresent: Boolean,
        iconPosition: ButtonIconPosition,
        loading: Boolean,
    ) {
        /** Properties are [Int]s and not [Dp]s because .[dp] calls will clutter the code */
        var textStartDp = 16
        var textEndDp = 16
        var iconStartDp = 0
        var iconEndDp = 0

        if (isIconPresent || loading) when {
            iconPosition == ButtonIconPosition.START || loading -> {
                textStartDp = 8
                iconStartDp = 16
            }
            iconPosition == ButtonIconPosition.END -> {
                textEndDp = 8
                iconEndDp = 16
            }
            else -> error("Unknown icon position: $iconPosition")
        }

        textStart = textStartDp.dp
        textEnd = textEndDp.dp
        iconStart = iconStartDp.dp
        iconEnd = iconEndDp.dp
    }
}
