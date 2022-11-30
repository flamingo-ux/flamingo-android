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

package com.flamingo.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.theme.FlamingoIcon

/**
 * Animates between [color] changes.
 *
 * Using [icon], it is possible to display an icon inside the [Indicator]. Our designers invented
 * a __novel__ concept: only a __small__ subset of all [FlamingoIcon]s are correctly displayed
 * inside the [Indicator]. Edges of others are cut off because of the small [Indicator] size. Beware
 * of this behaviour and __always check__ how your particular icon is displayed.
 *
 * @param animationSpec if null, no animation is applied when color is changed
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.IndicatorComposePreview",
    figma = "https://f.com/file/sPbkUbBGkp5Mstc0IQYubk/4.1.-UI-Android-kit?node-id=670%3A8",
    specification = "https://confluence.companyname.ru/x/ehEnKQE",
    viewImplementation = "com.flamingo.view.components.Indicator",
    demo = ["com.flamingo.playground.components.indicator.StatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Indicator(
    size: IndicatorSize = IndicatorSize.SMALL,
    color: IndicatorColor = IndicatorColor.DEFAULT,
    icon: FlamingoIcon? = null,
    animationSpec: AnimationSpec<Color>? = tween(400),
): Unit = Indicator(
    size = size,
    color = color,
    icon = icon,
    trench = false,
    animationSpec = animationSpec,
)

/**
 * @param trench if true, transparent border around [Indicator] is displayed
 */
@Composable
internal fun Indicator(
    size: IndicatorSize = IndicatorSize.SMALL,
    color: IndicatorColor = IndicatorColor.DEFAULT,
    icon: FlamingoIcon? = null,
    trench: Boolean = false,
    animationSpec: AnimationSpec<Color>? = tween(400),
): Unit = FlamingoComponentBase {
    val backgroundColor: Color = if (animationSpec != null) {
        animateColorAsState(color.backgroundColor, animationSpec).value
    } else color.backgroundColor

    val iconColor: Color = if (animationSpec != null) {
        animateColorAsState(color.iconColor, animationSpec).value
    } else color.iconColor

    val borderWidth = 2.dp
    Box(
        modifier = Modifier
            .requiredSize(size.size(icon != null).animateButtonDp())
            .run { if (trench) padding(borderWidth) else this }
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) Icon(
            modifier = Modifier.requiredSize(size.iconSize),
            icon = icon,
            tint = iconColor
        )
    }
}

public enum class IndicatorColor {
    DEFAULT, PRIMARY, ERROR, SECONDARY, WARNING;

    internal val backgroundColor: Color
        @ReadOnlyComposable
        @Composable
        get() = when (this) {
            DEFAULT -> Flamingo.colors.backgroundTertiary
            PRIMARY -> Flamingo.colors.primary
            ERROR -> Flamingo.colors.error
            SECONDARY -> Flamingo.colors.secondary
            WARNING -> Flamingo.colors.warning
        }

    internal val iconColor: Color
        @ReadOnlyComposable
        @Composable
        get() = when (this) {
            DEFAULT -> Flamingo.colors.textPrimary
            else -> Flamingo.colors.global.light.textPrimary
        }
}

public enum class IndicatorSize(
    internal val withoutIcon: Dp,
    internal val withIcon: Dp,
    internal val iconSize: Dp,
) {
    SMALL(withoutIcon = 8.dp, withIcon = 28.dp, iconSize = 16.dp),
    BIG(withoutIcon = 12.dp, withIcon = 32.dp, iconSize = 24.dp),
    ;

    public fun size(iconPresence: Boolean): Dp = if (iconPresence) withIcon else withoutIcon
}

public enum class IndicatorPlacement {
    TopStart, TopEnd, BottomStart, BottomEnd,
    ;

    internal val cutoutPlacement: CutoutPlacement
        get() = when (this) {
            TopStart -> CutoutPlacement.TopStart
            TopEnd -> CutoutPlacement.TopEnd
            BottomStart -> CutoutPlacement.BottomStart
            BottomEnd -> CutoutPlacement.BottomEnd
        }

    public val isEnd: Boolean get() = this == TopEnd || this == BottomEnd
    public val isStart: Boolean get() = this == TopStart || this == BottomStart

    public val isTop: Boolean get() = this == TopStart || this == TopEnd
    public val isBottom: Boolean get() = this == BottomStart || this == BottomEnd
}
