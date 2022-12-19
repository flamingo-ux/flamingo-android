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
    "ComplexMethod",
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.IconButtonColor.DEFAULT
import com.flamingo.components.IconButtonColor.ERROR
import com.flamingo.components.IconButtonColor.PRIMARY
import com.flamingo.components.IconButtonColor.RATING
import com.flamingo.components.IconButtonColor.WARNING
import com.flamingo.components.IconButtonColor.WHITE
import com.flamingo.components.IconButtonVariant.CONTAINED
import com.flamingo.components.IconButtonVariant.TEXT
import com.flamingo.components.button.animateButtonColor
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoRippleTheme

/**
 * If [loading] is true, [disabled] is also true (even if false is passed to the function).
 *
 * [WHITE] [color] MUST BE used when [IconButton] will be placed on dark backgrounds, gradients or
 * images/videos. White will not be switched to black in the dark theme
 *
 * @param indicator is always shown at the corner of the centered box with size
 * [IconButtonSize.SMALL], regardless of the [size]
 *
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this IconButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this IconButton in different [Interaction]s.
 *
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 */
@FlamingoComponent(
    displayName = "Icon Button",
    preview = "com.flamingo.playground.preview.IconButtonComposePreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/UI-kit?node-id=628%3A5",
    specification = "https://confluence.companyname.ru/x/iREnKQE",
    theaterPackage = "com.flamingo.playground.components.iconbutton.TheaterPkg",
    viewImplementation = "com.flamingo.view.components.IconButton",
    demo = ["com.flamingo.playground.components.iconbutton.IconButtonComposeStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.IconButton", "androidx.compose.material.IconToggleButton")
@Composable
public fun IconButton(
    onClick: () -> Unit,
    icon: FlamingoIcon,
    size: IconButtonSize = IconButtonSize.MEDIUM,
    variant: IconButtonVariant = CONTAINED,
    color: IconButtonColor = if (Flamingo.isWhiteMode) WHITE else DEFAULT,
    shape: IconButtonShape = IconButtonShape.CIRCLE,
    indicator: IconButtonIndicator? = null,
    loading: Boolean = false,
    disabled: Boolean = false,
    contentDescription: String?,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Unit = FlamingoComponentBase {
    @Suppress("NAME_SHADOWING") val disabled = disabled || loading
    val iconColor = calculateIconColor(variant, color)
    val rippleColor = calculateRippleColor(variant, color)
    val backgroundColor = calculateBackgroundColor(variant, color)

    val iconAlpha by animateFloatAsState(
        targetValue = if (!loading) 1f else 0f,
        animationSpec = spring(stiffness = 1100f)
    )

    val indicatorSize = when (size) {
        IconButtonSize.SMALL -> IndicatorSize.SMALL
        IconButtonSize.MEDIUM -> IndicatorSize.BIG
        IconButtonSize.LARGE -> IndicatorSize.BIG
    }
    val cutoutRadius = if (indicator != null) indicatorSize.withoutIcon / 2 else 0.dp
    val cutoutPlacement = indicator?.placement?.cutoutPlacement ?: CutoutPlacement.TopEnd

    Box(modifier = Modifier.alpha(disabled, animate = true)) {
        if (indicator != null) {
            IconButtonIndicator(
                size,
                shape,
                cutoutRadius,
                cutoutPlacement,
                indicator,
                indicatorSize
            )
        }

        Box(
            modifier = Modifier
                .requiredSize(size.size.animateDp())
                .clip(
                    RoundedRectWithCutoutShape(
                        cornerRadius = (if (shape == IconButtonShape.CIRCLE)
                            size.size / 2 else size.squareCorners).animateDp(),
                        cutoutRadius = cutoutRadius,
                        cutoutPlacement = cutoutPlacement,
                    )
                )
                .background(backgroundColor)
                .clickable(
                    onClick = onClick,
                    enabled = !disabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false, color = rippleColor),
                ),
            contentAlignment = Alignment.Center
        ) {
            if (loading) CircularLoader(size = CircularLoaderSize.SMALL, color = iconColor)
            Icon(
                modifier = Modifier.graphicsLayer { alpha = iconAlpha },
                icon = icon,
                tint = iconColor,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
private fun IconButtonIndicator(
    size: IconButtonSize,
    shape: IconButtonShape,
    cutoutRadius: Dp,
    cutoutPlacement: CutoutPlacement,
    indicator: IconButtonIndicator,
    indicatorSize: IndicatorSize,
) {
    Box(
        modifier = Modifier.circleOffset(
            rectSize = DpSize(size.size.animateDp(), size.size.animateDp()),
            cornerRadius = (if (shape == IconButtonShape.CIRCLE)
                size.size / 2 else size.squareCorners).animateDp(),
            circleRadius = cutoutRadius.animateDp(),
            cutoutPlacement = cutoutPlacement,
        )
    ) {
        Indicator(size = indicatorSize, color = indicator.color, trench = true)
    }
}

@Composable
private fun calculateBackgroundColor(
    variant: IconButtonVariant,
    color: IconButtonColor,
) = when (variant) {
    CONTAINED -> when (color) {
        DEFAULT -> Flamingo.colors.backgroundQuaternary
        PRIMARY -> Flamingo.colors.primary
        WARNING -> Flamingo.colors.warning
        RATING -> Flamingo.colors.rating
        ERROR -> Flamingo.colors.error
        WHITE -> Flamingo.palette.white
    }
    TEXT -> Color.Transparent
}.animateButtonColor()

@Composable
private fun calculateRippleColor(
    variant: IconButtonVariant,
    color: IconButtonColor,
) = when (variant) {
    CONTAINED -> FlamingoRippleTheme.defaultColor()
    TEXT -> when (color) {
        DEFAULT -> FlamingoRippleTheme.defaultColor()
        PRIMARY -> Flamingo.colors.primary
        WARNING -> Flamingo.colors.warning
        RATING -> Flamingo.colors.rating
        ERROR -> Flamingo.colors.error
        WHITE -> Flamingo.palette.white
    }
}

@Composable
private fun calculateIconColor(
    variant: IconButtonVariant,
    color: IconButtonColor,
) = when (variant) {
    CONTAINED -> when (color) {
        DEFAULT -> Flamingo.colors.textSecondary
        WHITE -> Flamingo.palette.grey850
        else -> Color.White
    }
    TEXT -> when (color) {
        DEFAULT -> Flamingo.colors.textSecondary
        PRIMARY -> Flamingo.colors.primary
        WARNING -> Flamingo.colors.warning
        RATING -> Flamingo.colors.rating
        ERROR -> Flamingo.colors.error
        WHITE -> Flamingo.palette.white
    }
}.animateButtonColor()

@Composable
internal fun Dp.animateDp(
    animationSpec: SpringSpec<Dp> = spring(stiffness = 700f),
): Dp = animateDpAsState(this, animationSpec = animationSpec).value

public enum class IconButtonSize(
    internal val size: Dp,
    internal val squareCorners: Dp
) { SMALL(32.dp, 8.dp), MEDIUM(40.dp, 8.dp), LARGE(48.dp, 12.dp) }

public enum class IconButtonColor { DEFAULT, PRIMARY, WARNING, RATING, ERROR, WHITE }
public enum class IconButtonVariant { CONTAINED, TEXT, }
public enum class IconButtonShape { CIRCLE, SQUARE, }

public data class IconButtonIndicator(
    val color: IndicatorColor,
    val placement: IndicatorPlacement = IndicatorPlacement.TopEnd,
)
