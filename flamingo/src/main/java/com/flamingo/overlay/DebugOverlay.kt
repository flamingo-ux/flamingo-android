@file:Suppress(
    "MatchingDeclarationName",
    "FunctionNaming",
)

package com.flamingo.overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.flamingo.Flamingo
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.overlay.DebugOverlay.Config

/**
 * A singleton, provided to components via [Flamingo.debugOverlay] and [Flamingo.LocalDebugOverlay].
 * Used to acquire [Config] and draw an overlay using default logic via [DebugOverlay].
 *
 * You can create your own implementation, customizing the default drawing logic and a source of
 * [Config]s.
 *
 * Default implementation is
 * @sample com.flamingo.playground.overlay.DebugOverlayImpl
 */
public interface DebugOverlay {
    /**
     * Provides [Config] for a particular component with [qualifiedFunctionNameOfTheComponent]
     * function name
     */
    public fun getConfig(qualifiedFunctionNameOfTheComponent: String): Config

    /**
     * Default drawing logic, that all components use by default via [FlamingoComponentBase]
     */
    @Composable
    public fun DebugOverlay(modifier: Modifier, config: Config, drawText: Boolean)

    @Composable
    public fun DebugOverlay(
        modifier: Modifier,
        config: Config,
        text: @Composable BoxScope.() -> Unit
    )

    /**
     * [Config] is used to provide an info about _what_ to draw in the debug overlay. Components can
     * use [Config] to draw (or not to draw) debug overlay differently, for example: change font
     * size an position.
     */
    public interface Config {
        public val borderStroke: BorderStroke
        public val backgroundColor: Color
        public val text: String
    }
}

/**
 * It is `internal` because otherwise it would be possible to accidentally provide `null`, and
 * global [Flamingo.debugOverlay] switch won't work.
 *
 * Can be used within the `flamingo` module to disable overlay for components inside other
 * components.
 *
 * @see Flamingo.LocalDebugOverlay
 */
internal val LocalDebugOverlayImpl = staticCompositionLocalOf<DebugOverlay?> { null }
