package com.flamingo.components.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastFirstOrNull
import kotlin.math.max

@Composable
internal fun ModalHeaderLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->

    val titleMeasurable = measurables.fastFirstOrNull { it.layoutId == "title" }
    val closeMeasurable = measurables.fastFirstOrNull { it.layoutId == "close" }

    val closePlaceable = closeMeasurable?.measure(Constraints())
    val closeHeight = closePlaceable?.height ?: 0
    val closeWidth = closePlaceable?.width ?: 0

    val titlePlaceable =
        titleMeasurable?.measure(Constraints(maxWidth = constraints.maxWidth - closeWidth))
    val titleHeight = titlePlaceable?.height ?: 0

    val titleWidth = if ((titlePlaceable?.width ?: 0) > constraints.maxWidth - closeWidth)
        constraints.maxWidth - closeWidth else titlePlaceable?.width ?: 0

    val totalHeight = max(titleHeight, closeHeight)
    val totalWidth = constraints.maxWidth

    layout(width = totalWidth, height = totalHeight) {
        closePlaceable?.placeRelative(
            x = totalWidth - closeWidth,
            y = totalHeight / 2 - closeHeight / 2
        )

        val offsetRaw = (totalWidth / 2 + titleWidth / 2) - (totalWidth - closeWidth)
        val offset = if (offsetRaw < 0) 0 else offsetRaw
        val titleX = totalWidth / 2 - titleWidth / 2 - offset

        titlePlaceable?.placeRelative(
            x = titleX,
            y = totalHeight / 2 - titleHeight / 2
        )
    }
}