@file:Suppress("SpacingAroundParens", "FunctionName")

package com.flamingo

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

/**
 * Allows applying shadow with opacity to [content].
 *
 * Just an experiment, CANNOT be used by library users.
 */
@Composable
public fun InternalComponents.Shadow(
    modifier: Modifier = Modifier,
    elevation: Dp,
    opacity: Float,
    shape: Shape,
    content: @Composable () -> Unit,
): Unit = Box(modifier) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .graphicsLayer {
                shadowElevation = elevation.toPx()
                alpha = opacity
                this.shape = shape
            }
    )
    content()
}
