package com.flamingo.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.flamingo.Flamingo

@Immutable
internal object FlamingoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color =
        (if (isSystemInDarkTheme()) Color.White else Flamingo.palette.blue850)

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = .12f,
        focusedAlpha = .12f,
        hoveredAlpha = .08f,
        pressedAlpha = .12f,
    )
}
