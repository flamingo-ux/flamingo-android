@file:Suppress("SpacingAroundParens", "FunctionNaming", "MagicNumber", "LongMethod")

package com.flamingo.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastFirstOrNull

/**
 * Layout for [Button]. Every child in [content] MUST have [layoutId] modifier with one of those
 * [String] values:
 * - background (required)
 * - text (required)
 * - startIcon or endIcon (optional)
 *
 * Number of children in [content] MUST be in range [2..3].
 *
 * Description of how this layout works is in [ButtonWidthPolicy] and [Button.fillMaxWidth] docs.
 */
@Composable
internal fun ButtonLayout(
    modifier: Modifier = Modifier,
    verticalPadding: Dp,
    fillMaxWidth: Boolean,
    widthPolicy: ButtonWidthPolicy,
    content: @Composable () -> Unit
) = Layout(content, modifier, object : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        require(measurables.size in 2..3) {
            "ButtonLayout supports only 2 or 3 children, but was: ${measurables.size}"
        }
        val verticalPaddingPx = verticalPadding.roundToPx()

        val iconMeasurable = measurables.getIcon()
        val textMeasurable = measurables.getText()
        val backgroundMeasurable = measurables.getBackground()

        val iconAtStart = (iconMeasurable?.layoutId as? String)?.startsWith("start")

        // icon cannot be cropped and will be displayed no matter what
        val iconPlaceable = iconMeasurable?.measure(Constraints())

        val iconWidth = iconPlaceable?.width ?: 0

        val textPlaceable = textMeasurable.measure(
            Constraints(
                maxWidth = if (widthPolicy == ButtonWidthPolicy.STRICT) {
                    Constraints.Infinity
                } else {
                    if (constraints.hasBoundedWidth) {
                        // if maxWidth < iconWidth, textWidth will be zero (no room for text)
                        (constraints.maxWidth - iconWidth).coerceAtLeast(0)
                    } else {
                        Constraints.Infinity
                    }
                }
            )
        )

        val textWidth = textPlaceable.width
        val textHeight = textPlaceable.height
        val iconHeight = iconPlaceable?.height ?: 0

        // can be larger than constraints.maxWidth, if maxWidth < iconWidth (which will always be
        // displayed)
        val contentWidth = iconWidth + textWidth

        // maxOf is used because contentWidth can be larger than constraints.maxWidth
        val totalWidth =
            if (fillMaxWidth) maxOf(contentWidth, constraints.maxWidth) else contentWidth

        val totalHeight = maxOf(iconHeight, textHeight) + verticalPaddingPx * 2

        val backgroundPlaceable = backgroundMeasurable.measure(
            Constraints.fixed(totalWidth, totalHeight)
        )

        return layout(width = totalWidth, height = totalHeight) {
            backgroundPlaceable.placeRelative(0, 0)
            val freeSideSpace = (totalWidth - contentWidth) / 2
            if (iconAtStart == true) {
                iconPlaceable?.placeRelative(
                    x = freeSideSpace,
                    y = totalHeight / 2 - iconHeight / 2 // means center vertically
                )
                textPlaceable.placeRelative(
                    x = freeSideSpace + iconWidth, // after icon
                    y = totalHeight / 2 - textHeight / 2
                )
            } else {
                textPlaceable.placeRelative(
                    x = freeSideSpace,
                    y = totalHeight / 2 - textHeight / 2
                )
                iconPlaceable?.placeRelative(
                    x = freeSideSpace + textWidth,
                    y = totalHeight / 2 - iconHeight / 2
                )
            }
        }
    }

    private fun List<Measurable>.getBackground(): Measurable = fastFirstOrNull {
        it.layoutId == "background"
    } ?: error("Child with layoutId == \"background\" is not found")

    private fun List<Measurable>.getText(): Measurable = fastFirstOrNull {
        it.layoutId == "text"
    } ?: error("Child with layoutId == \"text\" is not found")

    private fun List<Measurable>.getIcon(): Measurable? = fastFirstOrNull {
        (it.layoutId as String).endsWith("Icon")
    }
})
