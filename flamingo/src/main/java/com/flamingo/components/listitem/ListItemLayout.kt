@file:Suppress(
    "LongMethod",
    "SpacingAroundParens",
    "FunctionNaming",
)

package com.flamingo.components.listitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastFirstOrNull

/**
 * Layout for [ListItem]. Every child in [content] MUST have [layoutId] modifier with one of those
 * [String] values:
 * - start
 * - center
 * - end
 * - divider
 *
 * There MUST be no more than 4 children in [content].
 */
@Composable
internal fun ListItemScope.ListItemLayout(
    modifier: Modifier = Modifier,
    sideSlotsAlignment: SideSlotsAlignment,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    require(constraints.hasBoundedWidth) {
        "Width of the ListItem is unbounded (maxWidth == Constraints.Infinity). It is prohibited." +
                " Width should always be constrained."
    }

    val startMeasurable = measurables.fastFirstOrNull { it.layoutId == "start" }
    val centerMeasurable = measurables.fastFirstOrNull { it.layoutId == "center" }
    val endMeasurable = measurables.fastFirstOrNull { it.layoutId == "end" }
    val dividerMeasurable = measurables.fastFirstOrNull { it.layoutId == "divider" }

    val sideSlotConstraints = Constraints(
        maxWidth = constraints.maxWidth / 2,
        maxHeight = constraints.maxHeight,
    )

    val startPlaceable = startMeasurable?.measure(sideSlotConstraints)
    val endPlaceable = endMeasurable?.measure(sideSlotConstraints)

    val startWidth = startPlaceable?.width ?: 0
    val endWidth = endPlaceable?.width ?: 0

    val centerPlaceable = centerMeasurable?.measure(
        Constraints(
            minWidth = (constraints.maxWidth - startWidth - endWidth).coerceAtLeast(0),
            maxWidth = (constraints.maxWidth - startWidth - endWidth).coerceAtLeast(0),
            maxHeight = constraints.maxHeight,
        )
    )

    val centerWidth = centerPlaceable?.width ?: 0

    val vPaddingPx = vPadding.roundToPx()
    val dividerPlaceable = dividerMeasurable?.measure(
        Constraints(
            maxWidth = (centerWidth + endWidth).coerceAtMost(Int.MAX_VALUE),
            maxHeight = vPaddingPx,
        )
    )

    val startHeight = startPlaceable?.height ?: 0
    val centerHeight = centerPlaceable?.height ?: 0
    val endHeight = endPlaceable?.height ?: 0
    val dividerHeight = dividerPlaceable?.height ?: 0

    val maxSlotHeight = maxOf(startHeight, centerHeight, endHeight)
    val totalHeight = maxSlotHeight + vPaddingPx

    layout(width = constraints.maxWidth, height = totalHeight) {
        startPlaceable?.placeRelative(
            x = 0,
            y = slotY(sideSlotsAlignment, maxSlotHeight, startHeight),
        )
        centerPlaceable?.placeRelative(
            x = startWidth,
            y = slotY(SideSlotsAlignment.CENTER, maxSlotHeight, centerHeight),
        )
        endPlaceable?.placeRelative(
            x = startWidth + centerWidth,
            y = slotY(sideSlotsAlignment, maxSlotHeight, endHeight),
        )

        check(vPaddingPx - (dividerPlaceable?.measuredHeight ?: 0) >= 0) {
            "Divider is too tall: it must be shorter than ${vPadding}dp, but was " +
                    dividerHeight.toDp()
        }
        dividerPlaceable?.placeRelative(x = startWidth, y = totalHeight - dividerHeight)
    }
}

/**
 * Calculates y position of the slot.
 *
 * @param maxSlotHeight max height of all slots
 * @param slotHeight height of the slot for which y is calculated
 */
private fun slotY(
    sideSlotsAlignment: SideSlotsAlignment,
    maxSlotHeight: Int,
    slotHeight: Int
): Int = when (sideSlotsAlignment) {
    SideSlotsAlignment.TOP -> 0
    SideSlotsAlignment.CENTER -> maxSlotHeight / 2 - slotHeight / 2
    SideSlotsAlignment.BOTTOM -> maxSlotHeight - slotHeight
}
