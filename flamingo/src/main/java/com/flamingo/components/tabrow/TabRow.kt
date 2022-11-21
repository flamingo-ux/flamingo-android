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
    "LongMethod",
    "SpacingAroundParens"
)

package com.flamingo.components.tabrow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.Badge
import com.flamingo.components.BadgeColor
import com.flamingo.components.BadgeSize
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.Text
import com.flamingo.components.tabrow.TabVariant.Contained

/**
 * Must be placed form edge-to-edge of the screen, so then there would be no clipping of the
 * scrolling side of [TabRow].
 *
 * @param tabs size must be larger than 1
 * @param edgePadding to be removed, now uses 16.dp as default
 *
 * @sample com.flamingo.playground.components.tabrow.TwoSmallTabs
 * @sample com.flamingo.playground.components.tabrow.ManyTabs
 * @sample com.flamingo.playground.components.tabrow.ComplexTabs
 * @sample com.flamingo.playground.components.tabrow.ComplexLinedTabs
 * @sample com.flamingo.playground.components.tabrow.LinedTabs
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.TabRowPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=628%3A21",
    specification = "https://confluence.companyname.ru/x/GQJKWAE",
    theaterPackage = "com.flamingo.playground.components.tabrow.TheaterPkg",
    demo = ["com.flamingo.playground.components.tabrow.TabRowTypicalUsage"],
    supportsWhiteMode = false,
)
@UsedInsteadOf("androidx.compose.material.TabRow")
@Composable
public fun TabRow(
    tabs: Collection<String>,
    selectedTabIndex: Int,
    onTabSelect: (index: Int) -> Unit,
    variant: TabVariant = Contained,
    edgePadding: Dp = 16.dp,
): Unit = TabRow(
    tabs = tabs.map { Tab(it) },
    selectedTabIndex = selectedTabIndex,
    onTabSelect = onTabSelect,
    variant = variant,
)

/**
 * This overload allows displaying disabled tabs and tabs with [Badge].
 *
 * @see TabRow
 * @sample com.flamingo.playground.components.tabrow.ComplexTabs
 */
@Composable
public fun TabRow(
    tabs: List<Tab>,
    selectedTabIndex: Int,
    onTabSelect: (index: Int) -> Unit,
    variant: TabVariant = Contained,
    edgePadding: Dp = 16.dp,
): Unit = FlamingoComponentBase {
    require(tabs.size >= 2) { "TabRow doesn't support one tab. 2 or more are required" }

    TabRowBase(
        variant = variant,
        indicator = { tabPositions ->
            if (variant == Contained) {
                TabContainedIndicator(tabPositions[selectedTabIndex])
            } else {
                TabLinedIndicator(tabPositions[selectedTabIndex])
            }
        },
        selectedTabIndex = selectedTabIndex,
    ) {
        tabs.fastForEachIndexed { index, tab ->
            val selected = selectedTabIndex == index
            Tab(
                modifier = Modifier
                    .clip(if (variant == Contained) CircleShape else RoundedCornerShape(8.dp))
                    .alpha(tab.disabled, animate = true),
                selected = selected,
                enabled = !tab.disabled,
                variant = variant,
                onClick = { onTabSelect(index) },
            ) {
                val textColor by animateColorAsState(
                    getTabTextColor(variant, selected),
                    animationSpec = tween(300)
                )
                Text(
                    text = tab.label.replace("\n", " "),
                    style = if (variant == Contained) {
                        Flamingo.typography.body1
                    } else {
                        Flamingo.typography.h6
                    },
                    color = textColor,
                )
                with(tab.badge ?: return@Tab) {
                    Spacer(modifier = Modifier.requiredWidth(4.dp))
                    Badge(label, color, BadgeSize.SMALL)
                }
            }
        }
    }
}

public enum class TabVariant {
    Contained,

    @Deprecated("to be removed soon, DO NOT use it, replaced with TabVariant.Lined")
    Text,
    Lined
}

/**
 * @property label all `\n`s will be replaced with ` `
 * @property disabled if true, tab will not be clickable
 */
public data class Tab(
    val label: String,
    val disabled: Boolean = false,
    val badge: Badge? = null,
) {
    public data class Badge(val label: String, val color: BadgeColor)
}

@Composable
private fun TabContainedIndicator(tabPosition: TabPosition) = Box(
    Modifier
        .tabIndicatorOffset(tabPosition)
        .fillMaxSize()
        .clip(CircleShape)
        .background(Flamingo.colors.inverse.backgroundTertiary)
)

@Composable
private fun TabLinedIndicator(tabPosition: TabPosition) = Box(
    Modifier
        .tabIndicatorOffset(tabPosition)
        .fillMaxWidth()
        .height(4.dp)
        .background(Flamingo.colors.primary)
)

@Composable
private fun getTabTextColor(
    variant: TabVariant,
    selected: Boolean,
): Color = if (variant == Contained) {
    if (selected) Flamingo.colors.inverse.textPrimary else Flamingo.colors.textPrimary
} else {
    if (selected) Flamingo.colors.textPrimary else Flamingo.colors.textSecondary
}
