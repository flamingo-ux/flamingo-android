@file:Suppress("ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens")

package com.flamingo.theme.colors

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.flamingo.Flamingo

@Composable
internal fun ProvideFlamingoColors(colors: FlamingoColors, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFlamingoColors provides colors, content = content)
}

internal val LocalFlamingoColors = staticCompositionLocalOf { lightColors }

internal val lightColors = with(Flamingo.palette) {
    FlamingoColors(
        isLight = true,
        primary = green600,
        secondary = blue600,
        info = blue700,
        warning = orange600,
        success = green500,
        rating = yellow300,
        error = red700,
        separator = grey850.copy(alpha = .08f),
        outline = grey850.copy(alpha = .16f),
        outlineDark = grey850.copy(alpha = .32f),
        background = white,
        backgroundSecondary = grey100,
        backgroundTertiary = grey150,
        backgroundTextField = grey500.copy(alpha = .08f),
        backgroundQuaternary = grey800.copy(alpha = .08f),
        backgroundQuinary = green500.copy(alpha = .08f),
        backdrop = black.copy(alpha = .68f),
        textPrimary = black.copy(alpha = .87f),
        textSecondary = black.copy(alpha = .60f),
        textTertiary = black.copy(alpha = .38f),
        greenHover = green800,
        global = GlobalColors(
            light = GlobalColors.GlobalColorsVersion(
                textPrimary = white.copy(alpha = .98f),
                textSecondary = white.copy(alpha = .60f),
                textTertiary = white.copy(alpha = .38f),
                backgroundPrimary = white,
                backgroundSecondary = grey100,
                backgroundTertiary = grey150,
            ),
            dark = GlobalColors.GlobalColorsVersion(
                textPrimary = black.copy(alpha = .87f),
                textSecondary = black.copy(alpha = .60f),
                textTertiary = black.copy(alpha = .38f),
                backgroundPrimary = grey1000,
                backgroundSecondary = grey900,
                backgroundTertiary = grey850,
            ),
        ),
        inverse = InverseColors(
            textPrimary = white.copy(alpha = .98f),
            textSecondary = white.copy(alpha = .60f),
            textTertiary = white.copy(alpha = .38f),
            backgroundPrimary = grey1000,
            backgroundSecondary = grey900,
            backgroundTertiary = grey850,
        ),
        extensions = ExtensionColors(
            background = ExtensionColors.Background(
                error = red700.copy(alpha = .03f),
                info = blue700.copy(alpha = .04f),
                success = green500.copy(alpha = .04f),
                warning = orange700.copy(alpha = .03f),
                rating = yellow250.copy(alpha = .08f),
            ),
            outline = ExtensionColors.Outline(
                error = red700.copy(alpha = .08f),
                info = blue700.copy(alpha = .08f),
                success = green500.copy(alpha = .08f),
                warning = orange700.copy(alpha = .08f),
            ),
        )
    )
}

internal val darkColors = with(Flamingo.palette) {
    FlamingoColors(
        isLight = false,
        primary = green400,
        secondary = blue400,
        info = blue400,
        warning = orange500,
        success = green500,
        rating = yellow200,
        error = red500,
        separator = white.copy(alpha = .08f),
        outline = white.copy(alpha = .16f),
        outlineDark = white.copy(alpha = .32f),
        background = grey1000,
        backgroundSecondary = grey900,
        backgroundTertiary = grey850,
        backgroundTextField = white.copy(alpha = 0.12f),
        backgroundQuaternary = white.copy(alpha = 0.16f),
        backgroundQuinary = green500.copy(alpha = .24f),
        backdrop = black.copy(alpha = .68f),
        textPrimary = white.copy(alpha = .98f),
        textSecondary = white.copy(alpha = .60f),
        textTertiary = white.copy(alpha = .38f),
        greenHover = green600,
        global = GlobalColors(
            light = GlobalColors.GlobalColorsVersion(
                textPrimary = white.copy(alpha = .98f),
                textSecondary = white.copy(alpha = .60f),
                textTertiary = white.copy(alpha = .38f),
                backgroundPrimary = white,
                backgroundSecondary = grey100,
                backgroundTertiary = grey150,
            ),
            dark = GlobalColors.GlobalColorsVersion(
                textPrimary = black.copy(alpha = .87f),
                textSecondary = black.copy(alpha = .60f),
                textTertiary = black.copy(alpha = .38f),
                backgroundPrimary = grey1000,
                backgroundSecondary = grey900,
                backgroundTertiary = grey850,
            ),
        ),
        inverse = InverseColors(
            textPrimary = black.copy(alpha = .87f),
            textSecondary = black.copy(alpha = .60f),
            textTertiary = black.copy(alpha = .38f),
            backgroundPrimary = white,
            backgroundSecondary = grey100,
            backgroundTertiary = grey150,
        ),
        extensions = ExtensionColors(
            background = ExtensionColors.Background(
                error = red400.copy(alpha = .03f),
                info = blue400.copy(alpha = .04f),
                success = green400.copy(alpha = .04f),
                warning = orange400.copy(alpha = .03f),
                rating = yellow250.copy(alpha = .08f),
            ),
            outline = ExtensionColors.Outline(
                error = red400.copy(alpha = .08f),
                info = blue400.copy(alpha = .08f),
                success = green400.copy(alpha = .08f),
                warning = orange400.copy(alpha = .08f),
            ),
        )
    )
}

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [Flamingo.colors].
 */
internal fun debugColors(darkTheme: Boolean, debugColor: Color) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme,
)
