package com.flamingo.components.chipgroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import com.flamingo.components.Chip

/**
 * Layout for Chips inside [ChipGroup]. It should have at least one [Chip] inside.
 * __NOTE__ Components inside [content] should have the same height for layout to work properly
 */
@Composable
internal fun ChipGroupContentLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->

    require(measurables.isNotEmpty()) {
        "ChipGroup must have at least 1 Chip!"
    }

    val placeables = measurables.fastMap {
        it.measure(Constraints(maxWidth = constraints.maxWidth, maxHeight = constraints.maxHeight))
    }

    val horizontalPaddingPx = 8.dp.roundToPx()
    val verticalPaddingPx = 12.dp.roundToPx()

    var totalRows = 1
    var tmpWidth = 0
    placeables.forEachIndexed { index, placeable ->
        tmpWidth += placeable.width
        if (tmpWidth > constraints.maxWidth) {
            totalRows++
            tmpWidth = placeable.width + horizontalPaddingPx
        } else if (tmpWidth == constraints.maxWidth && index != placeables.size - 1) {
            totalRows++
            tmpWidth = 0
        } else {
            tmpWidth += horizontalPaddingPx
        }
    }

    // NOTE Only works if placeables have the same height!
    val maxHeight = placeables.maxBy { it.height }.height

    layout(
        width = constraints.maxWidth,
        height = totalRows * maxHeight + (totalRows - 1) * verticalPaddingPx
    ) {
        var currRow = 0
        var currX = 0
        placeables.forEach {
            if (currX != 0 && currX + it.width > constraints.maxWidth) {
                currX = 0
                currRow++
            }
            it.placeRelative(
                x = currX,
                y = currRow * (maxHeight + verticalPaddingPx)
            )
            currX += it.width + horizontalPaddingPx
        }
    }
}
