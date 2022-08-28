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

package com.flamingo.components.topappbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flamingo.Flamingo
import com.flamingo.Flamingo.isWhiteMode
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarSize
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Search
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.internalComponents
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoTheme
import com.flamingo.theme.colors.FlamingoColors

/**
 * Navigation component, placed at the top of the screen.
 *
 * It is forbidden to place [ActionItem]s simultaneously with [EdgeItem.Button].
 *
 * In [Flamingo.LocalWhiteMode], shadow and background won't be shown, regardless of [showShadow]
 * and [showBackground] values.
 *
 * If you are going to use [TopAppBar] in a lazy list or a [verticalScroll] list, use corresponding
 * overloads that manage shadow automatically.
 *
 * @param showShadow if true, and [showBackground] is true, and [Flamingo.LocalWhiteMode] is false,
 * shadow will be drawn. Changes of this parameter are animated
 *
 * @param showBackground if true, and [Flamingo.LocalWhiteMode] is false, background of the
 * [TopAppBar] will be [FlamingoColors.background]. Else - transparent
 *
 * @sample com.flamingo.playground.components.topappbar.ScrollingShadowSample
 * @sample com.flamingo.playground.components.topappbar.LazyColumnScrollingShadowSample
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.TopAppBarPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=11906%3A132158",
    specification = "https://confluence.companyname.ru/x/FoBJPAE",
    theaterPackage = "com.flamingo.playground.components.topappbar.TheaterPkg",
    demo = [
        "com.flamingo.playground.components.topappbar.TopAppBarStatesPlayroom",
        "com.flamingo.playground.components.topappbar.TopAppBarLazyListScrollDemo",
        "com.flamingo.playground.components.topappbar.TopAppBarScrollDemo",
        "com.flamingo.playground.components.topappbar.TopAppBarSearchFocusDemo",
    ],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.TopAppBar")
@Composable
public fun TopAppBar(
    start: EdgeItem? = null,
    center: CenterItem? = null,
    action1: ActionItem? = null,
    action2: ActionItem? = null,
    end: EdgeItem? = null,
    showShadow: Boolean = false,
    showBackground: Boolean = true,
): Unit = FlamingoComponentBase {
    if (end is EdgeItem.Button) require(action1 == null && action2 == null) {
        "It is forbidden to place [ActionItem]s simultaneously with [EdgeItem.Button]"
    }
    Box(contentAlignment = Alignment.BottomCenter) {
        SmallShadow(showShadow, showBackground)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeightIn(min = 56.dp)
                .run {
                    when {
                        isWhiteMode -> this
                        showBackground -> background(Flamingo.colors.background)
                        else -> this
                    }
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EdgeItem(start)
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                CenterItem(center)
            }
            Actions(action1, action2)
            EdgeItem(end)
        }
    }
}

/**
 * Shadow [Box]:
 * 1. has height of [4.dp] (less than 54.dp - min height of the [TopAppBar])
 * 2. aligned to the bottom of the outer box, because shadow is needed only at the bottom of the
 * [TopAppBar], not at the top
 * 3. is drawn behind the actual [TopAppBar] content (which has a non-transparent background)
 */
@Composable
private fun SmallShadow(showShadow: Boolean, showBackground: Boolean) = Box(
    modifier = Modifier
        .requiredHeight(4.dp)
        .fillMaxWidth()
        .run {
            val shadowElevation by animateDpAsState(
                targetValue = if (showShadow) 6.dp else 0.dp,
                animationSpec = tween(200)
            )
            when {
                isWhiteMode -> this
                showBackground -> shadow(shadowElevation)
                else -> this
            }
        }
)

/**
 * When you place [TopAppBar] in a lazy list, use this overload which automatically manages shadow:
 * when scroll position is > 0, shadow is shown, else - hidden.
 *
 * @see TopAppBar
 * @sample com.flamingo.playground.components.topappbar.LazyColumnScrollingShadowSample
 */
@Composable
public fun TopAppBar(
    start: EdgeItem? = null,
    center: CenterItem? = null,
    action1: ActionItem? = null,
    action2: ActionItem? = null,
    end: EdgeItem? = null,
    listState: LazyListState,
): Unit = TopAppBar(
    start = start,
    center = center,
    action1 = action1,
    action2 = action2,
    end = end,
    showShadow = remember {
        // https://youtu.be/eDcGrY_AVlw?t=2793
        derivedStateOf {
            listState.firstVisibleItemScrollOffset != 0 || listState.firstVisibleItemIndex > 0
        }
    }.value
)

/**
 * When you place [TopAppBar] in a [verticalScroll] list, use this overload which automatically
 * manages shadow: when scroll position is > 0, shadow is shown, else - hidden.
 *
 * @see TopAppBar
 * @sample com.flamingo.playground.components.topappbar.ScrollingShadowSample
 */
@Composable
public fun TopAppBar(
    start: EdgeItem? = null,
    center: CenterItem? = null,
    action1: ActionItem? = null,
    action2: ActionItem? = null,
    end: EdgeItem? = null,
    scrollState: ScrollState,
): Unit = TopAppBar(
    start = start,
    center = center,
    action1 = action1,
    action2 = action2,
    end = end,
    // https://youtu.be/eDcGrY_AVlw?t=2793
    showShadow = remember { derivedStateOf { scrollState.value > 0 } }.value
)

@Composable
private fun EdgeItem(edgeItem: EdgeItem?) = when (edgeItem) {
    is EdgeItem.Avatar -> with(edgeItem) {
        Spacer(16.dp)
        Avatar(
            content = content,
            onClick = onClick,
            shape = shape,
            size = AvatarSize.SIZE_32,
            contentDescription = null,
        )
        Spacer(16.dp)
    }
    is EdgeItem.IconButton -> with(edgeItem) {
        Spacer(8.dp)
        IconButtonWithIndicator(edgeItem.icon, indicator, disabled, onClick)
        Spacer(8.dp)
    }
    is EdgeItem.Button -> with(edgeItem) {
        Spacer(8.dp)
        Button(
            onClick = onClick,
            label = label,
            variant = ButtonVariant.TEXT,
            widthPolicy = ButtonWidthPolicy.TRUNCATING,
            color = if (isWhiteMode) ButtonColor.White else ButtonColor.TopAppBar
        )
        Spacer(8.dp)
    }
    is EdgeItem.Anything -> {
        Spacer(8.dp)
        edgeItem.content()
        Spacer(8.dp)
    }
    null -> Spacer(16.dp)
}

@Composable
private fun CenterItem(centerItem: CenterItem?) = when (centerItem) {
    is CenterItem.AdvancedCenter -> {
        if (centerItem.avatar != null) {
            Avatar(
                content = centerItem.avatar.content,
                shape = centerItem.avatar.shape,
                size = AvatarSize.SIZE_32,
                indicator = centerItem.avatar.indicator,
                contentDescription = null,
            )
            Spacer(8.dp)
        }
        if (centerItem.title != null || centerItem.subtitle != null) Column {
            val textBlock: @Composable () -> Unit = {
                if (centerItem.title != null) Text(
                    text = centerItem.title,
                    color = Flamingo.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Flamingo.typography.h5.copy(fontWeight = FontWeight.W600)
                )
                if (centerItem.subtitle != null) Text(
                    text = centerItem.subtitle,
                    color = Flamingo.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Flamingo.typography.caption2
                )
            }
            if (isWhiteMode) {
                FlamingoTheme(darkTheme = true) { textBlock() }
            } else textBlock()
        }
        Unit
    }
    is CenterItem.Search -> internalComponents.Search(
        value = centerItem.value,
        onValueChange = centerItem.onValueChange,
        onClick = centerItem.onClick,
        placeholder = centerItem.placeholder,
        loading = centerItem.loading,
        disabled = centerItem.disabled,
        keyboardOptions = centerItem.keyboardOptions,
        keyboardActions = centerItem.keyboardActions,
        focusRequester = centerItem.focusRequester
    )
    is CenterItem.SearchWithTextFieldValue -> internalComponents.Search(
        value = centerItem.value,
        onValueChange = centerItem.onValueChange,
        onClick = centerItem.onClick,
        placeholder = centerItem.placeholder,
        loading = centerItem.loading,
        disabled = centerItem.disabled,
        keyboardOptions = centerItem.keyboardOptions,
        keyboardActions = centerItem.keyboardActions,
        focusRequester = centerItem.focusRequester
    )
    is CenterItem.Anything -> centerItem.content()
    null -> Unit
}

@Composable
private fun Actions(action1: ActionItem?, action2: ActionItem?) {
    if (action1 != null || action2 != null) {
        Spacer(8.dp)
        action1?.apply { IconButtonWithIndicator(icon, indicator, disabled, onClick) }
        if (action1 != null && action2 != null) Spacer(8.dp)
        action2?.apply { IconButtonWithIndicator(icon, indicator, disabled, onClick) }
    }
}

@Composable
private fun IconButtonWithIndicator(
    icon: FlamingoIcon,
    indicator: IconButtonIndicator?,
    disabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        icon = icon,
        size = IconButtonSize.MEDIUM,
        indicator = indicator,
        variant = IconButtonVariant.TEXT,
        disabled = disabled,
        contentDescription = null
    )
}

@Composable
private fun Spacer(size: Dp) {
    val debug = Flamingo.LocalDebugMode.current
    Box(
        modifier = Modifier
            .requiredSize(size)
            .run { if (debug) background(Color.Red) else this },
        contentAlignment = Alignment.Center
    ) {
        if (debug) {
            Text(
                text = size.value.toInt().toString(),
                color = Color.White,
                fontSize = 8.sp,
            )
        }
    }
}
