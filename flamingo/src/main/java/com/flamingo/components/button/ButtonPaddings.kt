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
        hasStartItem: Boolean,
        hasEndItem: Boolean,
        loading: Boolean,
        size: ButtonSize
    ) {
        val sizePadding = if (size == ButtonSize.LARGE) 16 else 12
        /** Properties are [Int]s and not [Dp]s because .[dp] calls will clutter the code */
        var textStartDp = sizePadding
        var textEndDp = sizePadding
        var iconStartDp = 0
        var iconEndDp = 0

        when {
            hasStartItem || loading -> {
                textStartDp = 8
                iconStartDp = sizePadding
            }
            hasEndItem -> {
                textEndDp = 8
                iconEndDp = sizePadding
            }
        }

        textStart = textStartDp.dp
        textEnd = textEndDp.dp
        iconStart = iconStartDp.dp
        iconEnd = iconEndDp.dp
    }
}
