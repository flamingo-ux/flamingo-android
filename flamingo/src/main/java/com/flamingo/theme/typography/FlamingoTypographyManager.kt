// @file:Suppress("NoMultipleSpaces", "NoTrailingSpaces", "MagicNumber")
package com.flamingo.theme.typography

import androidx.compose.material.Typography
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.flamingo.Flamingo
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.components.LocalFlamingoTextStyle
import com.flamingo.components.Text
import com.flamingo.theme.colors.lightColors

/**
 * FORBIDDEN to use in client code, can only be used in `flamingo-*****-font` modules.
 */
@DelicateFlamingoApi
public object FlamingoTypographyManager {

    /**
     * Used in `flamingo-*****-font` modules to provide typography.
     * FORBIDDEN to use in client code, can only be used in `flamingo-*****-font` modules.
     * Can only be called once, subsequent invocations will be ignored.
     */
    @DelicateFlamingoApi
    public fun provideTypography(provider: FlamingoTypographyProvider) {
        if (::typographyProvider.isInitialized) return
        typographyProvider = provider
    }

    internal lateinit var typographyProvider: FlamingoTypographyProvider
        private set

    /**
     * This CompositionLocal holds on to the current definition of typography for this application
     * as described by the design system. Flamingo components related to text (such as [Text]) will
     * use this CompositionLocal to set values with which to style children text components.
     *
     * To access values within this CompositionLocal, __always__ use [Flamingo.typography].
     * @see LocalFlamingoTextStyle
     */
    internal val LocalFlamingoTypography: ProvidableCompositionLocal<FlamingoTypography> =
        staticCompositionLocalOf { robotoTypographyProvider.invoke(lightColors) }

    /** @see debugColors */
    internal fun debugTypography(debugColor: Color) = Typography(
        h1 = TextStyle(color = debugColor),
        h2 = TextStyle(color = debugColor),
        h3 = TextStyle(color = debugColor),
        h4 = TextStyle(color = debugColor),
        h5 = TextStyle(color = debugColor),
        h6 = TextStyle(color = debugColor),
        subtitle1 = TextStyle(color = debugColor),
        subtitle2 = TextStyle(color = debugColor),
        body1 = TextStyle(color = debugColor),
        body2 = TextStyle(color = debugColor),
        button = TextStyle(color = debugColor),
        caption = TextStyle(color = debugColor),
        overline = TextStyle(color = debugColor),
    )
}
