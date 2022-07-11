@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens"
)

package com.flamingo.components.widgetcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.LinkCard
import com.flamingo.fastSumByIndexed
import kotlin.math.ceil
import kotlin.math.roundToInt

public val WidgetCardGroupChildrenRange: IntRange = 1..6

/**
 * Container that places [WidgetCard]s. They can be displayed only using [WidgetCardGroup].
 * Number of children in [widgetCards] MUST BE in the range of [WidgetCardGroupChildrenRange].
 * You can call __ONLY__ these [Composable] functions inside [widgetCards]: [WidgetCard], [LinkCard]
 * Anything else will result in a runtime exception.
 *
 * For example, this __is not__ allowed:
 * ```
 * WidgetCardGroup {
 *     Box {
 *         WidgetCard()
 *     }
 * }
 * ```
 *
 * @sample com.flamingo.playground.components.widgetcardgroup.Sample1
 * @sample com.flamingo.playground.components.widgetcardgroup.Sample2
 */
@FlamingoComponent(
    displayName = "Widget Card Group",
    preview = "com.flamingo.playground.preview.WidgetCardGroupPreview",
    figma = "https://todo.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=24622%3A148578",
    specification = "https://todo.com/x/_VwF3QE",
    demo = ["com.flamingo.playground.components.widgetcardgroup.Demo"],
    supportsWhiteMode = false,
)
@Composable
public fun WidgetCardGroup(
    widgetCards: @Composable WidgetCardGroupScope.() -> Unit,
): Unit = FlamingoComponentBase {
    Layout({ WidgetCardGroupScopeImpl.widgetCards() }) { measurables, constraints ->
        requireValidCardsNumber(measurables)
        measurables.fastForEach {
            require(it.layoutId == WidgetCardLayoutId) {
                "Only WidgetCards can be children of WidgetCardGroup"
            }
        }
        val measurePolicy = when {
            constraints.maxWidth < 840.dp.toPx() -> twoColumnsMeasurePolicy
            else -> gridMeasurePolicy
        }
        with(measurePolicy) { measure(measurables, constraints) }
    }
}

public class WidgetCardGroupScope internal constructor()

private val gridMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val columns = if (measurables.size == 1) 2 else minOf(measurables.size, 4)
    val cardsPaddingPx = paddingBetweenCards.roundToPx()
    val fixedHeightBrickHeightPx = fixedHeightBrickHeight.roundToPx()
    val placeables = measurables.fastMap {
        it.measure(constraints.fixedHeightBrick(
            columns, cardsPaddingPx, fixedHeightBrickHeightPx
        ))
    }

    val rows = (ceil(placeables.size.toDouble() / columns)).toInt()
    layout(
        width = constraints.maxWidth,
        height = rows * (fixedHeightBrickHeightPx + cardsPaddingPx) - cardsPaddingPx,
    ) {
        var height = 0
        placeables.forEachIndexed { index, placeable ->
            val columnIndex = index % columns
            placeable.placeRelative(
                x = columnIndex * placeable.width + columnIndex * cardsPaddingPx,
                y = height,
            )
            if (columnIndex == columns - 1) {
                // last column in this row
                height += fixedHeightBrickHeightPx + cardsPaddingPx
            }
        }
    }
}
private val twoColumnsMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val cardsPaddingPx = paddingBetweenCards.roundToPx()
    val placeables = buildList(measurables.size) {
        when (measurables.size) {
            1 -> {
                add(measurables[0].measure(constraints.brick(cardsPaddingPx)))
            }
            2 -> {
                add(measurables[0].measure(constraints.square(cardsPaddingPx)))
                add(measurables[1].measure(constraints.square(cardsPaddingPx)))
            }
            3 -> {
                add(measurables[0].measure(constraints.square(cardsPaddingPx)))
                add(measurables[1].measure(constraints.square(cardsPaddingPx)))
                add(measurables[2].measure(constraints.brick(cardsPaddingPx)))
            }
            4 -> {
                add(measurables[0].measure(constraints.square(cardsPaddingPx)))
                add(measurables[1].measure(constraints.monolith(cardsPaddingPx)))
                add(measurables[3].measure(constraints.monolith(cardsPaddingPx)))
                add(measurables[2].measure(constraints.square(cardsPaddingPx)))
            }
            5 -> {
                add(measurables[0].measure(constraints.square(cardsPaddingPx)))
                add(measurables[1].measure(constraints.monolith(cardsPaddingPx)))
                add(measurables[3].measure(constraints.monolith(cardsPaddingPx)))
                add(measurables[2].measure(constraints.square(cardsPaddingPx)))

                add(measurables[4].measure(constraints.brick(cardsPaddingPx)))
            }
            6 -> {
                add(measurables[0].measure(constraints.square(cardsPaddingPx)))
                add(measurables[1].measure(constraints.monolith(cardsPaddingPx)))
                add(measurables[3].measure(constraints.monolith(cardsPaddingPx)))
                add(measurables[2].measure(constraints.square(cardsPaddingPx)))

                add(measurables[4].measure(constraints.square(cardsPaddingPx)))
                add(measurables[5].measure(constraints.square(cardsPaddingPx)))
            }
            else -> requireValidCardsNumber(measurables)
        }
    }
    layout(
        width = constraints.maxWidth,
        height = placeables.fastSumByIndexed { index, placeable ->
            if (index % 2 == 0) placeable.height + cardsPaddingPx else 0
        } - cardsPaddingPx // because placeables.size >= 1
    ) {
        val secondColumnXOffset = constraints.maxWidth / 2 + cardsPaddingPx / 2
        var firstColumnHeight = 0
        var secondColumnHeight = 0
        placeables.forEachIndexed { index, placeable ->
            if (index % 2 == 0) {
                // placing at start
                placeable.placeRelative(x = 0, firstColumnHeight)
                firstColumnHeight += placeable.height + cardsPaddingPx
            } else {
                // placing at end
                placeable.placeRelative(x = secondColumnXOffset, secondColumnHeight)
                secondColumnHeight += placeable.height + cardsPaddingPx
            }
        }
    }
}

private fun Constraints.square(cardsPaddingPx: Int): Constraints {
    val squareSideLength = (maxWidth - cardsPaddingPx).coerceAtLeast(0) / 2
    return Constraints.fixed(width = squareSideLength, height = squareSideLength)
}

private fun Constraints.monolith(cardsPaddingPx: Int): Constraints {
    val width = (maxWidth - cardsPaddingPx).coerceAtLeast(0) / 2
    return Constraints.fixed(width = width, height = (width * (4f / 3)).roundToInt())
}

private fun Constraints.brick(cardsPaddingPx: Int): Constraints = Constraints.fixed(
    width = maxWidth,
    height = (maxWidth - cardsPaddingPx).coerceAtLeast(0) / 2
)

private fun Constraints.fixedHeightBrick(
    columns: Int,
    cardsPaddingPx: Int,
    fixedHeightBrickHeightPx: Int,
): Constraints {
    val cardsWidth = (maxWidth.toFloat() - cardsPaddingPx * (columns - 1)).coerceAtLeast(0f)
    return Constraints.fixed(
        width = (cardsWidth / columns).roundToInt(),
        height = fixedHeightBrickHeightPx
    )
}

private val fixedHeightBrickHeight = 168.dp
private val WidgetCardGroupScopeImpl = WidgetCardGroupScope()
private val paddingBetweenCards = 16.dp
private fun requireValidCardsNumber(measurables: List<Measurable>) =
    require(measurables.size in WidgetCardGroupChildrenRange) {
        "Number of WidgetCards must be in the range of $WidgetCardGroupChildrenRange"
    }
