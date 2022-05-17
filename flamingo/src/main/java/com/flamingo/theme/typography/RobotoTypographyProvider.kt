@file:Suppress("NoMultipleSpaces", "NoTrailingSpaces", "MagicNumber")

package com.flamingo.theme.typography

import androidx.compose.runtime.CompositionLocal
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.theme.typography.FlamingoTypographyManager.LocalFlamingoTypography

/**
 * FORBIDDEN to use in client code, can only be used in `flamingo-*****-font` modules.
 *
 * [robotoTypographyProvider] is placed in `flamingo` module instead of `flamingo-roboto-font`
 * module because of the need to have a default value of the [LocalFlamingoTypography]
 * [CompositionLocal], so that composable @[Preview]s can compile by themselves, without
 * `flamingo-roboto-font` dependency.
 */
@DelicateFlamingoApi
public val robotoTypographyProvider: FlamingoTypographyProvider = FlamingoTypographyProvider {
    val colors = it
    FlamingoTypography(
        colors = colors,
        display1 =  /**/ roboto(96, 96 * 1.00000000, -0.01900000000, 500, colors.textPrimary),
        display2 =  /**/ roboto(60, 64 * 1.00000000, -0.01900000000, 500, colors.textPrimary),
        display3 =  /**/ roboto(48, 52 * 1.00000000, -0.01900000000, 500, colors.textPrimary),
        h1 =        /**/ roboto(34, 34 * 0.99608550, +0.00735294117, 700, colors.textPrimary),
        h2 =        /**/ roboto(28, 28 * 0.97000000, +0.00892857142, 600, colors.textPrimary),
        h3 =        /**/ roboto(22, 22 * 1.08606951, +0.00000000000, 600, colors.textPrimary),
        h4 =        /**/ roboto(22, 22 * 1.08606951, +0.00000000000, 600, colors.textPrimary),
        h5 =        /**/ roboto(20, 20 * 1.02000000, +0.00750000000, 700, colors.textPrimary),
        h6 =        /**/ roboto(16, 16 * 1.06000000, +0.01562500000, 600, colors.textPrimary),
        body1 =     /**/ roboto(17, 17 * 1.22000000, +0.02941176470, 400, colors.textSecondary),
        body2 =     /**/ roboto(15, 15 * 1.13778711, +0.01666666666, 400, colors.textSecondary),
        subtitle1 = /**/ roboto(16, 16 * 1.27000000, +0.00937500000, 400, colors.textPrimary),
        subtitle2 = /**/ roboto(15, 15 * 1.13778711, +0.00666666666, 600, colors.textPrimary),
        caption =   /**/ roboto(12, 12 * 1.13778711, +0.03333333333, 400, colors.textPrimary),
        overline =  /**/ roboto(11, 11 * 1.24122230, +0.03636363636, 500, colors.textPrimary),
        button =    /**/ roboto(14, 16 * 1.00000000, +0.00000000000, 600, colors.textPrimary),
        button2 =   /**/ roboto(14, 16 * 1.00000000, +0.00000000000, 600, colors.textPrimary),
    )
}

/**
 * @param fontSize in [sp]
 * @param lineHeight in [sp]
 * @param letterSpacing in [em]
 * @param fontWeight in a range of [1, 1000]
 */
private fun roboto(
    fontSize: Int,
    lineHeight: Double,
    letterSpacing: Double,
    fontWeight: Int,
    color: Color,
): TextStyle = TextStyle(
    fontSize = fontSize.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.em,
    fontWeight = FontWeight(fontWeight),
    color = color,
)
