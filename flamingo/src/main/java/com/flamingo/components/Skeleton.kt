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

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.theme.FlamingoTheme
import com.flamingo.theme.typography.FlamingoTypography
import com.flamingo.utils.exhaustive

/**
 * A placeholder for the content. Typically displayed when content is loading.
 *
 * It is __FORBIDDEN__ to use [clip] and [graphicsLayer] modifiers on [modifier].
 *
 * @param modifier It is __FORBIDDEN__ to use [clip] and [graphicsLayer] modifiers
 *
 * @sample com.flamingo.playground.components.skeleton.Circle
 * @sample com.flamingo.playground.components.skeleton.Text
 * @sample com.flamingo.playground.components.skeleton.Rectangle
 * @sample com.flamingo.playground.components.skeleton.RectangleNoRoundCorners
 * @sample com.flamingo.playground.components.skeleton.Square
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.SkeletonPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=4520%3A23658",
    specification = "https://confluence.companyname.ru/x/14uKMQE",
    demo = ["com.flamingo.playground.components.skeleton.SkeletonTypicalUsage"],
    supportsWhiteMode = true,
)
@UsedInsteadOf(
    "com.google.accompanist.placeholder.material.placeholder",
    "com.google.accompanist.placeholder.placeholder"
)
@Composable
public fun Skeleton(
    modifier: Modifier = Modifier,
    shape: SkeletonShape = SkeletonShape.Rectangle()
): Unit = FlamingoComponentBase {
    val animatedAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(750, easing = animEasing), RepeatMode.Reverse)
    )

    val skeleton: @Composable () -> Unit = {
        Box(
            modifier = modifier
                .run {
                    when (shape) {
                        is SkeletonShape.Text -> {
                            val textBlockLineHeight = with(LocalDensity.current) {
                                shape.textStyle.lineHeight
                                    .toDp()
                                    .coerceAtLeast(MIN_SKELETON_SIZE)
                            }
                            requiredHeight(textBlockLineHeight).clip(shape.shape)
                        }
                        else -> clip(shape.shape)
                    }.exhaustive
                }
                .graphicsLayer { alpha = animatedAlpha }
                .background(Flamingo.colors.backgroundTertiary)
        )
    }

    Box {
        if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = false) { skeleton() } else skeleton()
    }
}

@Immutable
public sealed class SkeletonShape(internal val shape: Shape) {
    private constructor(cornerRadiusMultiplier: Int) :
            this(RoundedCornerShape(cornerRadiusMultiplier.dp * 4))

    public object Circle : SkeletonShape(CircleShape)

    /**
     * @param cornerRadiusMultiplier this number will be multiplied by 4 to get final corner radius
     */
    @Immutable
    public class Rectangle(cornerRadiusMultiplier: Int = 1) : SkeletonShape(cornerRadiusMultiplier)

    /**
     * Used when [Skeleton] is replacing text content.
     *
     * @param cornerRadiusMultiplier this number will be multiplied by 4 to get final corner radius
     * @property textStyle MUST BE one of [FlamingoTypography] styles
     */
    @Immutable
    public class Text(cornerRadiusMultiplier: Int = 1, public val textStyle: TextStyle) :
        SkeletonShape(cornerRadiusMultiplier)

    @Immutable
    internal class AnyShape(shape: Shape) : SkeletonShape(shape)
}

private val animEasing = CubicBezierEasing(.37f, 0f, .63f, 1f)
private val MIN_SKELETON_SIZE: Dp = 8.dp
