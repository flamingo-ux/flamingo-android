package com.flamingo.components.alertmessage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import java.lang.Integer.max

/**
 * Layout for [AlertMessage] actions.
 * Every child in [content] MUST have [layoutId] modifier with one of those [String] values:
 * - firstAction
 * - secondAction
 *
 * There MUST be no more than 2 children in [content].
 */
@Composable
internal fun AlertMessageActionsLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->

    require(measurables.size < 3) {
        "AlertMessageActionsLayout supports only up to 2 children, but was: ${measurables.size}"
    }

    val firstActionMeasurable = measurables.fastFirstOrNull { it.layoutId == "firstAction" }
    val secondActionMeasurable = measurables.fastFirstOrNull { it.layoutId == "secondAction" }

    val hasFirstAction = firstActionMeasurable != null
    val hasSecondAction = secondActionMeasurable != null

    val actionConstraints = Constraints(
        maxWidth = constraints.maxWidth,
        maxHeight = constraints.maxHeight,
    )

    val firstActionPlaceable = firstActionMeasurable?.measure(actionConstraints)
    val secondActionPlaceable = secondActionMeasurable?.measure(actionConstraints)

    val firstActionWidth = firstActionPlaceable?.width ?: 0
    val secondActionWidth = secondActionPlaceable?.width ?: 0

    val firstActionHeight = firstActionPlaceable?.height ?: 0
    val secondActionHeight = secondActionPlaceable?.height ?: 0

    val horizontalSpacingPx = if (hasSecondAction) 24.dp.roundToPx() else 0

    val totalWidth = firstActionWidth + secondActionWidth + horizontalSpacingPx

    val isMultiline = totalWidth > constraints.maxWidth

    val totalHeight = if (isMultiline) firstActionHeight + secondActionHeight
    else max(firstActionHeight, secondActionHeight)

    layout(width = constraints.maxWidth, height = totalHeight) {
        if (isMultiline) {
            firstActionPlaceable?.placeRelative(x = 0, y = 0)
            secondActionPlaceable?.placeRelative(
                x = 0,
                y = firstActionHeight
            )
        } else {
            if (hasFirstAction) {
                firstActionPlaceable?.placeRelative(x = 0, y = 0)
            }
            if (hasSecondAction) {
                secondActionPlaceable?.placeRelative(
                    x = firstActionWidth + horizontalSpacingPx,
                    y = 0
                )
            }
        }
    }
}
