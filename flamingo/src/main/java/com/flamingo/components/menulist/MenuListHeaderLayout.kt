package com.flamingo.components.menulist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastFirstOrNull
import kotlin.math.max

@Composable
internal fun MenuListHeaderLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->

    val titleMeasurable = measurables.fastFirstOrNull { it.layoutId == "title" }
    val backMeasurable = measurables.fastFirstOrNull { it.layoutId == "back" }

    val backPlaceable = backMeasurable?.measure(Constraints())
    val backHeight = backPlaceable?.height ?: 0
    val backWidth = backPlaceable?.width ?: 0

    val titlePlaceable =
        titleMeasurable?.measure(Constraints(maxWidth = constraints.maxWidth - backWidth))
    val titleHeight = titlePlaceable?.height ?: 0

    val titleWidth = if ((titlePlaceable?.width ?: 0) > constraints.maxWidth - backWidth)
        constraints.maxWidth - backWidth else titlePlaceable?.width ?: 0

    val totalHeight = max(titleHeight, backHeight)
    val totalWidth = constraints.maxWidth

    layout(width = totalWidth, height = totalHeight) {
        backPlaceable?.placeRelative(
            x = 0,
            y = totalHeight / 2 - backHeight / 2
        )

        val offsetRaw = (totalWidth / 2 + titleWidth / 2) - (totalWidth - backWidth)
        val offset = if (offsetRaw < 0) 0 else offsetRaw
        val titleX = totalWidth / 2 - titleWidth / 2 + offset

        titlePlaceable?.placeRelative(
            x = titleX,
            y = totalHeight / 2 - titleHeight / 2
        )
    }
}