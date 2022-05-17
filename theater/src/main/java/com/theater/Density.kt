@file:Suppress("FunctionNaming", "SpacingAroundParens")

package com.theater

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

/**
 * [Actor]s must be of the same:
 * 1. pixel size on all screens, if [enforceSize] is true;
 * 2. size, relative to the [Stage] size.
 */
@Composable
internal fun ProvideDensity(
    play: TheaterPlay<*, *>,
    enforceSize: Boolean,
    content: @Composable (density: Density, width: Dp, height: Dp) ->
    Unit,
) = with(play.sizeConfig) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var densityMultiplier = 0f
        val density = if (enforceSize) {
            density
        } else {
            densityMultiplier = minOf(
                constraints.maxWidth.toFloat() / size.width,
                constraints.maxHeight.toFloat() / size.height
            )
            Density(density.density * densityMultiplier, density.fontScale)
        }

        val width = calculateDimension(enforceSize, density, densityMultiplier, size.width)
        val height = calculateDimension(enforceSize, density, densityMultiplier, size.height)

        CompositionLocalProvider(LocalDensity provides density) {
            content(density, width, height)
        }
    }
}

private fun calculateDimension(
    enforceSize: Boolean,
    density: Density,
    densityMultiplier: Float,
    dimension: Int,
) = if (enforceSize) {
    Dp(dimension.toFloat() / density.density)
} else {
    with(density) { (dimension * densityMultiplier).toDp() }
}

private operator fun DpOffset.component1(): Dp = x
private operator fun DpOffset.component2(): Dp = y
