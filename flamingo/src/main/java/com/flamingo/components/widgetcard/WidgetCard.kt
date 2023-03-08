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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.InternalComponents
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Skeleton
import com.flamingo.components.SkeletonShape
import com.flamingo.components.Text
import com.flamingo.components.UiTestingTagWrapper

/**
 * Can only be displayed using [WidgetCardGroup].
 *
 * One of these MUST NOT be null: [title], [subtitle].
 * Only one of these CAN BE non-null: [indicator], [iconButton]
 *
 * @param textAtTheTop if true, [title] and [subtitle] are displayed at the top of the card, and
 * [avatar], [indicator] and [iconButton] — at the bottom.
 * @param backgroundColor displayed __behind__ the [backgroundImage]
 * @param loading if true, skeleton is displayed instead of te card
 *
 * @see WidgetCardGroup
 */
@FlamingoComponent(
    displayName = "Widget Card",
    preview = "com.flamingo.playground.preview.WidgetCardPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=16720%3A132437",
    specification = "https://confluence.companyname.ru/x/_VwF3QE",
    demo = ["com.flamingo.playground.components.widgetcard.Demo"],
    supportsWhiteMode = false,
)
@Composable
public fun WidgetCardGroupScope.WidgetCard(
    title: String?,
    subtitle: String? = null,
    textColor: Color = Flamingo.colors.textPrimary,
    textAtTheTop: Boolean = true,
    backgroundColor: Color = Flamingo.colors.backgroundSecondary,
    backgroundImage: Painter? = null,
    srcImage: Painter? = null,
    loading: Boolean = false,
    avatar: (@Composable () -> Unit)? = null,
    indicator: (@Composable () -> Unit)? = null,
    iconButton: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
): Unit = WidgetCard(
    modifier = Modifier.layoutId(WidgetCardLayoutId), sizeModifier = Modifier.fillMaxSize(),
    title, subtitle, textColor, textAtTheTop, backgroundColor, backgroundImage, srcImage,
    loading, avatar, indicator, iconButton, onClick,
)

@Composable
public fun InternalComponents.WidgetCard(
    size: WidgetCardSize,
    title: String?,
    subtitle: String? = null,
    textColor: Color = Flamingo.colors.textPrimary,
    textAtTheTop: Boolean = true,
    backgroundColor: Color = Flamingo.colors.backgroundSecondary,
    backgroundImage: Painter? = null,
    srcImage: Painter? = null,
    loading: Boolean = false,
    avatar: (@Composable () -> Unit)? = null,
    indicator: (@Composable () -> Unit)? = null,
    iconButton: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
): Unit = WidgetCard(
    modifier = Modifier, sizeModifier = Modifier.cardSize(size),
    title, subtitle, textColor, textAtTheTop, backgroundColor, backgroundImage, srcImage,
    loading, avatar, indicator, iconButton, onClick,
)

@Composable
internal fun WidgetCard(
    modifier: Modifier,
    sizeModifier: Modifier,
    title: String?,
    subtitle: String? = null,
    textColor: Color = Flamingo.colors.textPrimary,
    textAtTheTop: Boolean = true,
    backgroundColor: Color = Flamingo.colors.backgroundSecondary,
    backgroundImage: Painter? = null,
    srcImage: Painter? = null,
    loading: Boolean = false,
    avatar: (@Composable () -> Unit)? = null,
    indicator: (@Composable () -> Unit)? = null,
    iconButton: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Flamingo.checkFlamingoPresence()
    require(title != null || subtitle != null) {
        "At least one of these params must be non-null: title, subtitle"
    }
    require(!(indicator != null && iconButton != null)) {
        "Only one of these params can be non-null: indicator, iconButton"
    }
    val funName = "com.flamingo.components.widgetcard.WidgetCard"
    Box(modifier) {
        UiTestingTagWrapper(funName) {
            Box {
                if (loading) {
                    Skeleton(
                        modifier = sizeModifier,
                        shape = SkeletonShape.Rectangle(WidgetCardCornerRadius / 4)
                    )
                } else {
                    CardContent(
                        sizeModifier, backgroundColor, onClick, backgroundImage, srcImage,
                        textAtTheTop, title, subtitle, textColor, avatar, indicator, iconButton
                    )
                }

                val debugOverlay = Flamingo.LocalDebugOverlay.current
                if (debugOverlay != null) with(debugOverlay) {
                    DebugOverlay(
                        modifier = Modifier.matchParentSize(),
                        config = getConfig(funName),
                        drawText = true
                    )
                }
            }
        }
    }
}

@Composable
private fun CardContent(
    modifier: Modifier,
    backgroundColor: Color,
    onClick: (() -> Unit)?,
    backgroundImage: Painter?,
    srcImage: Painter?,
    textAtTheTop: Boolean,
    title: String?,
    subtitle: String?,
    textColor: Color,
    avatar: @Composable (() -> Unit)?,
    indicator: @Composable (() -> Unit)?,
    iconButton: @Composable (() -> Unit)?,
) {
    Box(
        modifier = modifier
            .background(backgroundColor, WidgetCardShape)
            .clip(WidgetCardShape)
            .run { if (onClick != null) clickable(onClick = onClick) else this },
        contentAlignment = if (textAtTheTop) Alignment.BottomEnd else Alignment.TopEnd
    ) {
        if (backgroundImage != null) {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = backgroundImage,
                contentDescription = null,
                contentScale = FillBounds
            )
        } else if (srcImage != null) {
            Image(
                modifier = Modifier
                    .aspectRatio(1f)
                    .imageLayout(textAtTheTop),
                painter = srcImage,
                contentDescription = null,
                contentScale = Crop
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            if (textAtTheTop) TextBlock(Modifier.weight(1f), title, subtitle, textColor)
            if (avatar != null || indicator != null || iconButton != null) {
                AvatarBlock(
                    modifier = if (textAtTheTop) Modifier else Modifier.weight(1f),
                    verticalAlignment =
                    if (textAtTheTop) Alignment.Bottom else Alignment.Top,
                    avatar = avatar,
                    second = indicator ?: iconButton
                )
            }
            if (!textAtTheTop) TextBlock(Modifier, title, subtitle, textColor)
        }
    }
}

private fun Modifier.imageLayout(textAtTheTop: Boolean) = layout { measurable, constraints ->
    val maxAspectRatio = constraints.maxWidth / constraints.maxHeight.coerceAtLeast(1)
    val squareSideLength = minOf(constraints.maxWidth, constraints.maxHeight)
    val placeable = measurable.measure(Constraints.fixed(squareSideLength, squareSideLength))
    layout(constraints.maxWidth, constraints.maxHeight) {
        if (maxAspectRatio >= 1f) {
            // card is wide
            // placing at the end of the card
            placeable.placeRelative(
                x = (constraints.maxWidth - placeable.width).coerceAtLeast(0),
                y = 0
            )
        } else {
            // card is tall
            if (textAtTheTop) {
                // placing at the bottom of the card
                placeable.placeRelative(
                    x = 0,
                    y = (constraints.maxHeight - placeable.height).coerceAtLeast(0)
                )
            } else {
                // placing at the top of the card
                placeable.placeRelative(x = 0, y = 0)
            }
        }
    }
}

@Composable
private fun TextBlock(
    modifier: Modifier,
    title: String? = null,
    subtitle: String? = null,
    textColor: Color,
) = Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
    var subtitleMaxLines by remember { mutableStateOf(2) }
    if (title != null) Text(
        text = title,
        style = Flamingo.typography.h6,
        color = textColor,
        maxLines = 2,
        onTextLayout = { if (subtitle != null) subtitleMaxLines = if (it.lineCount > 1) 1 else 2 },
        overflow = TextOverflow.Ellipsis
    )
    if (subtitle != null) Text(
        text = subtitle,
        style = Flamingo.typography.caption2,
        color = textColor,
        maxLines = subtitleMaxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun AvatarBlock(
    modifier: Modifier,
    verticalAlignment: Alignment.Vertical,
    avatar: (@Composable () -> Unit)?,
    second: (@Composable () -> Unit)?,
) = Row(modifier, verticalAlignment = verticalAlignment) {
    Box(Modifier.weight(1f)) { avatar?.invoke() }
    Box(Modifier) { second?.invoke() }
}


internal object WidgetCardLayoutId

private const val WidgetCardCornerRadius = 20
internal val WidgetCardShape = RoundedCornerShape(WidgetCardCornerRadius.dp)
