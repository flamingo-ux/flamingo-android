@file:Suppress(
    "LongParameterList",
    "SpacingAroundOperators",
    "SpacingAroundParens",
    "FunctionNaming",
    "MagicNumber",
    "MatchingDeclarationName",
    "LongMethod",
)

package com.flamingo.components.tabrow

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Differs from the [androidx.compose.material.ScrollableTabRow] mainly in:
 * 1. placement order of
 *     1. indicator
 *     2. divider
 *     3. tabs
 * 2. measuring of the divider
 * 3. no [MaterialTheme]
 */
@Composable
internal fun TabRowBase(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    variant: TabVariant,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit,
    tabs: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val scrollableTabData = remember(scrollState, coroutineScope) {
        ScrollableTabData(
            scrollState = scrollState,
            coroutineScope = coroutineScope
        )
    }
    SubcomposeLayout(
        modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.CenterStart)
            .horizontalScroll(scrollState)
            .selectableGroup()
            .clipToBounds()
    ) { constraints ->
        val minTabWidth = ScrollableTabRowMinimumTabWidth.roundToPx()
        val padding = edgePadding.roundToPx()
        val inner = innerPadding(variant).roundToPx()
        val tabConstraints = constraints.copy(minWidth = minTabWidth)

        val tabPlaceables = subcompose(TabSlots.Tabs, tabs)
            .fastMap { it.measure(tabConstraints) }

        var layoutWidth = padding * 2 + inner * (tabPlaceables.size - 1)
        var layoutHeight = 0
        tabPlaceables.fastForEach {
            layoutWidth += it.width
            layoutHeight = maxOf(layoutHeight, it.height)
        }

        // Position the children.
        layout(layoutWidth, layoutHeight) {
            // Place the tabs on top of the indicator
            val tabPositions = mutableListOf<TabPosition>()
            var left = padding
            tabPlaceables.fastForEach {
                it.placeRelative(left, 0, zIndex = 2f)
                tabPositions.add(TabPosition(left = left.toDp(), width = it.width.toDp()))
                left += it.width + inner
            }

            // The divider is measured to fill the entire space occupied by the tab row and
            // placed.
//            subcompose(TabSlots.Divider).fastForEach {
//                val placeable =
//                    it.measure(Constraints.fixed(layoutWidth - padding * 2, layoutHeight))
//                placeable.placeRelative(padding, 0, zIndex = 0f)
//            }

            // The indicator container is measured to fill the entire space occupied by the tab
            // row, and then placed on top of the divider.
            subcompose(TabSlots.Indicator) {
                indicator(tabPositions)
            }.fastForEach {
                val placeable = it.measure(Constraints.fixed(layoutWidth, layoutHeight))
                placeable.placeRelative(0, 0, zIndex = 1f)
            }

            scrollableTabData.onLaidOut(
                density = this@SubcomposeLayout,
                edgeOffset = padding,
                tabPositions = tabPositions,
                selectedTab = selectedTabIndex
            )
        }
    }
}

/**
 * Data class that contains information about a tab's position on screen, used for calculating
 * where to place the indicator that shows which tab is selected.
 *
 * @property left the left edge's x position from the start of the [TabRow]
 * @property right the right edge's x position from the start of the [TabRow]
 * @property width the width of this tab
 */
@Immutable
internal class TabPosition internal constructor(val left: Dp, val width: Dp) {
    val right: Dp get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width)"
    }
}

/**
 * [Modifier] that takes up all the available width inside the [TabRow], and then animates
 * the offset of the indicator it is applied to, depending on the [currentTabPosition].
 *
 * @param currentTabPosition [TabPosition] of the currently selected tab. This is used to
 * calculate the offset of the indicator this modifier is applied to, as well as its width.
 */
internal fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPosition,
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

private enum class TabSlots {
    Tabs,
    Indicator
}

/**
 * Class holding onto state needed for [ScrollableTabRow]
 */
private class ScrollableTabData(
    private val scrollState: ScrollState,
    private val coroutineScope: CoroutineScope,
) {
    private var selectedTab: Int? = null

    fun onLaidOut(
        density: Density,
        edgeOffset: Int,
        tabPositions: List<TabPosition>,
        selectedTab: Int,
    ) {
        // Animate if the new tab is different from the old tab, or this is called for the first
        // time (i.e selectedTab is `null`).
        if (this.selectedTab != selectedTab) {
            this.selectedTab = selectedTab
            tabPositions.getOrNull(selectedTab)?.let {
                // Scrolls to the tab with [tabPosition], trying to place it in the center of the
                // screen or as close to the center as possible.
                val calculatedOffset = it.calculateTabOffset(density, edgeOffset, tabPositions)
                coroutineScope.launch {
                    scrollState.animateScrollTo(
                        calculatedOffset,
                        animationSpec = ScrollableTabRowScrollSpec
                    )
                }
            }
        }
    }

    /**
     * @return the offset required to horizontally center the tab inside this TabRow.
     * If the tab is at the start / end, and there is not enough space to fully centre the tab, this
     * will just clamp to the min / max position given the max width.
     */
    private fun TabPosition.calculateTabOffset(
        density: Density,
        edgeOffset: Int,
        tabPositions: List<TabPosition>,
    ): Int = with(density) {
        val totalTabRowWidth = tabPositions.last().right.roundToPx() + edgeOffset
        val visibleWidth = totalTabRowWidth - scrollState.maxValue
        val tabOffset = left.roundToPx()
        val scrollerCenter = visibleWidth / 2
        val tabWidth = width.roundToPx()
        val centeredTabOffset = tabOffset - (scrollerCenter - tabWidth / 2)
        // How much space we have to scroll. If the visible width is <= to the total width, then
        // we have no space to scroll as everything is always visible.
        val availableSpace = (totalTabRowWidth - visibleWidth).coerceAtLeast(0)
        return centeredTabOffset.coerceIn(0, availableSpace)
    }
}

private val ScrollableTabRowMinimumTabWidth = 0.dp
private val edgePadding = 16.dp

private fun innerPadding(variant: TabVariant) =
    if (variant == TabVariant.Contained) 12.dp else 24.dp

/**
 * [AnimationSpec] used when scrolling to a tab that is not fully visible.
 */
private val ScrollableTabRowScrollSpec: AnimationSpec<Float> = tween(
    durationMillis = 250,
    easing = FastOutSlowInEasing
)
