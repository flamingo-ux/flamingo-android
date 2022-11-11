package com.flamingo.components.listitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastFirstOrNull

@Composable
internal fun ListItemTextLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    require(constraints.hasBoundedWidth) {
        "Width of the ListItem is unbounded (maxWidth == Constraints.Infinity). It is prohibited." +
                " Width should always be constrained."
    }

    val textMeasurable = measurables.fastFirstOrNull { it.layoutId == "text" }
    val startIconMeasurable = measurables.fastFirstOrNull { it.layoutId == "textIconStart" }
    val endIconMeasurable = measurables.fastFirstOrNull { it.layoutId == "textIconEnd" }

    val sideSlotConstraints = Constraints()

    val startIconPlaceable = startIconMeasurable?.measure(sideSlotConstraints)
    val endIconPlaceable = endIconMeasurable?.measure(sideSlotConstraints)

    val startIconWidth = startIconPlaceable?.width ?: 0
    val endIconWidth = endIconPlaceable?.width ?: 0


    val textPlaceable = textMeasurable?.measure(
        Constraints(
            maxWidth = (constraints.maxWidth - startIconWidth - endIconWidth).coerceAtLeast(0),
        )
    )

    val textWidth = textPlaceable?.width ?: 0

    val startIconHeight = startIconPlaceable?.height ?: 0
    val textHeight = textPlaceable?.height ?: 0
    val endIconHeight = endIconPlaceable?.height ?: 0

    val maxSlotHeight = maxOf(startIconHeight, textHeight, endIconHeight)

    layout(width = constraints.maxWidth, height = maxSlotHeight) {
        val freeSideSpace = 0
        startIconPlaceable?.placeRelative(
            x = freeSideSpace,
            y = maxSlotHeight / 2 - startIconHeight / 2,
        )
        textPlaceable?.placeRelative(
            x = freeSideSpace + startIconWidth,
            y = maxSlotHeight / 2 - textHeight / 2,
        )
        endIconPlaceable?.placeRelative(
            x = freeSideSpace + startIconWidth + textWidth,
            y = maxSlotHeight / 2 - endIconHeight / 2,
        )
    }
}