@file:Suppress(
    "MatchingDeclarationName",
    "MagicNumber",
)

package com.flamingo.playground.overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.crab.FlamingoRegistry
import com.flamingo.overlay.DebugOverlay

internal class DebugOverlayImpl : DebugOverlay {
    private val borderStroke = BorderStroke(0.3.dp, Color.Red)
    private val backgroundColor = Color.Green.copy(alpha = 0.1f)

    /**
     * Uses [FlamingoRegistry] to get info about a particular component by its
     * [qualifiedFunctionNameOfTheComponent]
     */
    override fun getConfig(qualifiedFunctionNameOfTheComponent: String): DebugOverlay.Config {
        val record = FlamingoRegistry.components
            .find { it.funName == qualifiedFunctionNameOfTheComponent }
            ?: componentNotFound(qualifiedFunctionNameOfTheComponent)

        return DebugOverlayConfigImpl(
            borderStroke = borderStroke,
            backgroundColor = backgroundColor,
            text = record.displayName
        )
    }

    /**
     * Draws:
     * 1. Square border
     * 2. Background color with some alpha
     * 3. Debug text
     *     - Name of the component
     *
     * For more info, see [specification](https://todo.ru/x/dpiVSwE) of the debug
     * overlay.
     */
    @Composable
    override fun DebugOverlay(
        modifier: Modifier,
        config: DebugOverlay.Config,
        drawText: Boolean
    ) = DebugOverlay(
        modifier = modifier,
        config = config,
        text = inner@{
            if (!drawText) return@inner

            val fontSize = with(LocalDensity.current) { 20.toSp() }
            Text(
                text = config.text,
                style = Flamingo.typography.caption.copy(
                    fontSize = fontSize, lineHeight = fontSize
                ),
                overflow = TextOverflow.Visible,
            )
        }
    )

    /**
     * Draws:
     * 1. Square border
     * 2. Background color with some alpha
     * 3. [text]
     *
     * For more info, see [specification](https://todo.ru/x/dpiVSwE) of the debug
     * overlay.
     */
    @Composable
    override fun DebugOverlay(
        modifier: Modifier,
        config: DebugOverlay.Config,
        text: @Composable BoxScope.() -> Unit
    ) = Box(
        modifier = modifier
            .border(config.borderStroke)
            .background(config.backgroundColor)
            .padding(start = config.borderStroke.width + 0.5.dp),
        content = text
    )

    private fun componentNotFound(componentFunName: String): Nothing =
        error("Flamingo component with function name \"$componentFunName\" wasn't found")
}
