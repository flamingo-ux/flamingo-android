package com.flamingo

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.featureflags.FeatureFlagProvider
import com.flamingo.overlay.DebugOverlay
import com.flamingo.overlay.LocalDebugOverlayImpl
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoTheme
import com.flamingo.theme.LocalFlamingoPresence
import com.flamingo.theme.colors.FlamingoColorPalette
import com.flamingo.theme.colors.FlamingoColors
import com.flamingo.theme.colors.LocalFlamingoColors
import com.flamingo.theme.typography.FlamingoTypography
import com.flamingo.theme.typography.FlamingoTypographyManager.LocalFlamingoTypography

/**
 * Entry point of the Flamingo Design System.
 */
public object Flamingo {
    /**
     * Semantically-named list of colors that can change their values (e.g. light and dark themes).
     */
    public val colors: FlamingoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFlamingoColors.current

    /**
     * Semantically-named list of [TextStyle]s. Must be used with
     * [com.flamingo.components.Text] function.
     */
    public val typography: FlamingoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFlamingoTypography.current

    /**
     * Static non-semantically-named colors. Use __only__ in rare cases, when an object is placed on
     * top of the image, video or gradient. Prefer using [colors].
     */
    public val palette: FlamingoColorPalette = FlamingoColorPalette()

    public val icons: FlamingoIcon.FlamingoIcons = FlamingoIcon.FlamingoIcons()

    public val logos: FlamingoIcon.FlamingoLogos = FlamingoIcon.FlamingoLogos()

    /**
     * Some flamingo components support white mode, in which colors of the component become clearly
     * visible on dark backgrounds, images and videos.
     *
     * Support is indicated by [FlamingoComponent.supportsWhiteMode].
     */
    public val LocalWhiteMode: ProvidableCompositionLocal<Boolean> = compositionLocalOf { false }

    /**
     * Shortcut to get value of [LocalWhiteMode].
     */
    public val isWhiteMode: Boolean @Composable @ReadOnlyComposable get() = LocalWhiteMode.current

    /**
     * When true, components enable some debugging capabilities. Those capabilities are not
     * standardized, meaning that each component does whatever it wants when value of
     * [LocalDebugMode] is true. In addition, behaviour of the components is this mode is not
     * documented and __UNDEFINED__.
     *
     * Never use in production!
     */
    public val LocalDebugMode: ProvidableCompositionLocal<Boolean> =
        staticCompositionLocalOf { false }

    /**
     * When non-null, all flamingo components render debug info on top of them.
     *
     * For more info, see [specification](https://confluence.companyname.ru/x/dpiVSwE) of the debug
     * overlay.
     *
     * To see what exactly is drawn, refer to default overlay drawing implementation docs:
     * @sample com.flamingo.playground.overlay.DebugOverlayImpl.DebugOverlay
     * @see DebugOverlay
     * @see debugOverlay
     */
    public val LocalDebugOverlay: CompositionLocal<DebugOverlay?> get() = LocalDebugOverlayImpl

    /**
     * Used to enable/disable debug overlay globally, for all [FlamingoTheme] sub-hierarchies.
     *
     * You can use it in your app, but it is advised to use these functions from
     * `flamingo-playground` module: [com.flamingo.playground.overlay.enableDebugOverlay],
     * [com.flamingo.playground.overlay.disableDebugOverlay].
     *
     * ## Why not just use providable [LocalDebugOverlay]?
     *
     * There are (most certainly) many different and unconnected [Composable] hierarchies created
     * using [ComposeView]. Thus, a truly application-global switch is needed - [debugOverlay].
     *
     * But if [debugOverlay] will be used everywhere, instead of [LocalDebugOverlay], compose
     * machinery will create and maintain many subscriptions to [debugOverlay]'s [MutableState]
     * (see more in [staticCompositionLocalOf]).
     *
     * So, [LocalDebugOverlay] is used within one hierarchy. Value of [LocalDebugOverlayImpl] is set
     * in [FlamingoTheme].
     */
    public var debugOverlay: DebugOverlay? by mutableStateOf(null)

    /**
     * Global switch, enables usage of ui testing tags instead of normal [contentDescription]s.
     * Should only be used in non-production builds, as this mechanism __dramatically__ degrades
     * performance of the app.
     *
     * Must be set before any compose hierarchy is created (e.g. in [Application.onCreate])
     *
     * @see uiTestingTag
     */
    public var uiTestingTagsEnabled: Boolean = false

    @Suppress("MayBeConstant") // removal of the 'const' modifier is a breaking change
    public val versionName: String = BuildConfig.VERSION_NAME

    /**
     * Enables testing capabilities in the staging build of the client app.
     * Must be set before any compose hierarchy is created (e.g. in [Application.onCreate])
     */
    public var isStagingBuild: Boolean = false

    public var featureFlagProvider: FeatureFlagProvider = object : FeatureFlagProvider {}

    /**
     * Throws an exception if [FlamingoTheme] isn't found at the call site's position in the
     * hierarchy.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    @ReadOnlyComposable
    public fun checkFlamingoPresence() {
        if (LocalInspectionMode.current) return
        require(LocalFlamingoPresence.current) {
            "Flamingo theme wasn't found. You need to call flamingo components only in " +
                    "sub-hierarchy of com.flamingo.theme.FlamingoTheme function)"
        }
    }
}
