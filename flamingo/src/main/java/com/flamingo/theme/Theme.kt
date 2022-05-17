@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens"
)

package com.flamingo.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.flamingo.Flamingo
import com.flamingo.components.LocalFlamingoTextStyle
import com.flamingo.overlay.LocalDebugOverlayImpl
import com.flamingo.theme.colors.ProvideFlamingoColors
import com.flamingo.theme.colors.darkColors
import com.flamingo.theme.colors.debugColors
import com.flamingo.theme.colors.lightColors
import com.flamingo.theme.typography.FlamingoTypographyManager
import com.flamingo.theme.typography.FlamingoTypographyManager.LocalFlamingoTypography
import com.flamingo.theme.typography.FlamingoTypographyManager.debugTypography
import com.flamingo.theme.typography.robotoTypographyProvider

@Composable
public fun FlamingoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    applyDebugColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val debugColor = Color.Magenta
    val colors = if (darkTheme) darkColors else lightColors

    MaterialTheme(
        colors = if (applyDebugColor) debugColors(darkTheme, debugColor) else MaterialTheme.colors,
        typography = if (applyDebugColor) debugTypography(debugColor) else MaterialTheme.typography,
    ) {
        ProvideFlamingoColors(colors) {
            if (LocalInspectionMode.current) {
                FlamingoTypographyManager.provideTypography(robotoTypographyProvider)
            }
            val typography = runCatching {
                FlamingoTypographyManager.typographyProvider(Flamingo.colors)
            }.getOrElse { error(NO_TYPOGRAPHY_PROVIDER) }

            CompositionLocalProvider(
                LocalFlamingoPresence provides true,
                LocalDebugOverlayImpl provides Flamingo.debugOverlay,
                LocalRippleTheme provides FlamingoRippleTheme,
                LocalFlamingoTypography provides typography,
                LocalFlamingoTextStyle provides typography.body1,
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = Flamingo.colors.primary,
                    backgroundColor = Flamingo.colors.primary.copy(alpha = .4f)
                ),
                content = content
            )
        }
    }
}

/**
 * Used in [Flamingo.checkFlamingoPresence]
 */
internal val LocalFlamingoPresence = staticCompositionLocalOf { false }

private const val NO_TYPOGRAPHY_PROVIDER =
    "Cannot obtain typographyProvider. initRobotoTypography() or initSbsansTypography() must be" +
            " called before using FlamingoTheme."
