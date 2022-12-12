package com.flamingo.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.flamingo.Flamingo
import com.flamingo.components.BadgeColor
import com.flamingo.components.button.ButtonColor.Default
import com.flamingo.components.button.ButtonColor.Error
import com.flamingo.components.button.ButtonColor.Info
import com.flamingo.components.button.ButtonColor.Primary
import com.flamingo.components.button.ButtonColor.Success
import com.flamingo.components.button.ButtonColor.Warning
import com.flamingo.components.button.ButtonColor.White
import com.flamingo.components.button.ButtonVariant.CONTAINED
import com.flamingo.components.button.ButtonVariant.TEXT
import com.flamingo.theme.FlamingoRippleTheme

internal object ButtonColorCalculation {
    @Composable
    fun backgroundColor(variant: ButtonVariant, color: ButtonColor): Color = when (variant) {
        CONTAINED -> when (color) {
            Default -> Flamingo.colors.backgroundQuaternary
            Primary -> Flamingo.colors.primary
            Warning -> Flamingo.colors.warning
            Error -> Flamingo.colors.error
            Info -> Flamingo.colors.info
            Success -> Flamingo.colors.success
            ButtonColor.TopAppBar -> error("This configuration is currently unsupported")
            White -> Flamingo.palette.white
        }
        TEXT -> Color.Transparent
    }.animateButtonColor()

    @Composable
    fun rippleColor(variant: ButtonVariant, color: ButtonColor): Color = when (variant) {
        CONTAINED -> FlamingoRippleTheme.defaultColor()
        TEXT -> when (color) {
            Default, ButtonColor.TopAppBar -> FlamingoRippleTheme.defaultColor()
            Primary -> Flamingo.colors.primary
            Warning -> Flamingo.colors.warning
            Error -> Flamingo.colors.error
            Info -> Flamingo.colors.info
            Success -> Flamingo.colors.success
            White -> Flamingo.palette.white
        }
    }

    @Suppress("ComplexMethod")
    @Composable
    fun onColor(variant: ButtonVariant, color: ButtonColor): Color = when (variant) {
        CONTAINED -> when (color) {
            Default -> Flamingo.colors.textPrimary
            White -> Flamingo.palette.grey850
            ButtonColor.TopAppBar -> error("This configuration is currently unsupported")
            else -> Color.White
        }
        TEXT -> when (color) {
            Default -> Flamingo.colors.textPrimary
            Primary -> Flamingo.colors.primary
            Warning -> Flamingo.colors.warning
            Error -> Flamingo.colors.error
            Info -> Flamingo.colors.info
            Success -> Flamingo.colors.success
            White -> Flamingo.palette.white
            ButtonColor.TopAppBar -> Flamingo.colors.textSecondary
        }
    }.animateButtonColor()

    @Composable
    fun badgeColor(variant: ButtonVariant, color: ButtonColor) = when (variant) {
        CONTAINED -> when (color) {
            Default -> BadgeColor.Default
            White -> BadgeColor.White
            ButtonColor.TopAppBar -> error("This configuration is currently unsupported")
            else -> BadgeColor.White
        }
        TEXT -> when (color) {
            Default -> BadgeColor.Default
            Primary -> BadgeColor.Primary
            Warning -> BadgeColor.Warning
            Error -> BadgeColor.Error
            Info -> BadgeColor.Info
            Success -> BadgeColor.Primary //todo Badge success
            White -> BadgeColor.Default
            ButtonColor.TopAppBar -> BadgeColor.Default //todo Badge TopAppBar
        }
    }
}
